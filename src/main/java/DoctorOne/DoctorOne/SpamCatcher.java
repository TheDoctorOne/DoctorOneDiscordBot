package DoctorOne.DoctorOne;

import java.util.ArrayList;
import java.util.List;


import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.managers.GuildController;

public class SpamCatcher {

	private Guild guild;
	private GuildController controller;
	private User user;
	private Member member;
	private ArrayList<Message> messages;
	private List<Role> roles;
	private TextChannel LastTextChannel;
	private int time;
	public SpamCatcher(Message objMsg) {
		guild = objMsg.getGuild();
		controller = guild.getController();
		user = objMsg.getAuthor();
		member = objMsg.getMember();
		messages = new ArrayList<Message>();
		
		new Thread(new Runnable() {
			
			public void run() {
				while(true) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				time++;
					if(messages.size() > 2) {
						
						Role roleToAdd = null;
						List<Role> allRoles = guild.getRoles();
						for(Role r : allRoles) {
							if(r.getName().toLowerCase().equals("geçici ban")) {
								roleToAdd = r;
							}
						}
						ArrayList<Role> _roleToAdd = new ArrayList<Role>();
						_roleToAdd.add(roleToAdd);
						
						roles = member.getRoles();
						System.out.println(roles.toString());
						
						LastTextChannel.sendMessage("Spam YASAK! 10 SANİYE GEÇİCİ BAN : " + user.getAsMention() ).complete();
						
						controller.removeRolesFromMember(member, roles).complete();
						System.out.println("here! roles removed!");
						if(roleToAdd != null)
							controller.addSingleRoleToMember(member, roleToAdd).complete();
						else
							System.out.println("Role not found!");
						System.out.println("Ban role added!");
						
					try {
						Thread.sleep(10000);
						System.out.println("10 Secs have been passed.");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					
					controller.removeRolesFromMember(member, _roleToAdd).complete();
					System.out.println("Ban role REMOVED!");
					controller.addRolesToMember(member, roles).complete();
					System.out.println("here! roles are good to go!");
					messages.clear();
					time=0;
					}
					if(time >= 50) {
						messages.clear();
						time = 0;
					}
				}
			}
		}).start();
	}
	
	public User getUser() {
		return this.user;
	}
	
	public void addMessage(Message objMsg) {
		messages.add(objMsg);
		LastTextChannel = objMsg.getTextChannel();
		
	}
	
	public int getMessageNum() {
		return messages.size();
	}
}
