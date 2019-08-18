/*
 * INVITE LINK : https://discordapp.com/oauth2/authorize?client_id=473661328691822592&scope=bot&permissions=8
 * 
 * */

package DoctorOne.DoctorOne;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

//INVITE LINK
//https://discordapp.com/oauth2/authorize?client_id=473661328691822592&scope=bot
import javax.security.auth.login.LoginException;
import javax.swing.ImageIcon;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Game.GameType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.managers.AudioManager;

/**
 * Hello world!
 *
 */
public class App extends ListenerAdapter
{
	/*
	 * Variables
	 * */
	ArrayList<User> userList = new ArrayList<User>();
	static JDA DoctorOneBot;
	EmbedBuilder embed;
	ArrayList<Message> toDelete = new ArrayList<Message>();
	boolean deleteOK = false;
	boolean getSpawnMessage = false;
	boolean deleteLast2Message = false;
	static String LastPokemonURL;
	public static ArrayList<SpamCatcher> spammerList = new ArrayList<SpamCatcher>();
	public static ArrayList<User> addedUsers = new ArrayList<User>();
	//public static ArrayList<User> gunaydinList = new ArrayList<User>();
	//public static ArrayList<User> gecelerList = new ArrayList<User>();
    public static void main( String[] args ) throws LoginException, InterruptedException {
        DoctorOneBot = new JDABuilder(AccountType.BOT).setToken(References.TOKEN).setStatus(OnlineStatus.DO_NOT_DISTURB).buildBlocking();
        DoctorOneBot.addEventListener(new App());
        System.out.println(DoctorOneBot.getGuilds().size());
        DoctorOneBot.getPresence().setGame(Game.of(GameType.WATCHING, "MahmutKoçaş", "https://www.youtube.com/mahmutkocas/live"));
        //Konsol ile mesaj gönderme
        new Thread(new Runnable() {
			
			public void run() {
				while(true) {
					try {
						Thread.sleep(1000);
						Scanner scan = new Scanner(System.in);
						String s = scan.nextLine();
						if(!s.equals("")) {
							List<TextChannel> list = DoctorOneBot.getTextChannels();
							int i=0;
							for(TextChannel chan : list) {
								System.out.println(i + "." +chan);
								i++;}
							String s1 = scan.nextLine();
							TextChannel chan;
							if(!s1.equals("")) {
								chan = list.get(Integer.parseInt(s1));
								chan.sendMessage(s).queue();
								String s11 = BotUI.Logger.getText();
					    		BotUI.Logger.setText(s11 + "mesaj gönderildi " + chan + "\n");
								System.out.println("mesaj gönderildi " + chan);}
						}
						
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
		}).start();
        
        
        BotUI botui = new BotUI(DoctorOneBot);
    }
    
    /*
     * in-Bot Komutlar
     * */	
    private void deleteMessage() {
    	if(deleteOK) {
    		for(Message del : toDelete)
    			del.delete().queue();
    		deleteOK = false;
    	}
    }
    private void getMessage(Message msg) {
    	if(getSpawnMessage) {
    		toDelete.add(msg);
    		getSpawnMessage = false;
    	}
    }
    
    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
    	
    	/*
    	 * Gerekli Bilgiler
    	 * */
    	Message objMsg = e.getMessage();
    	MessageChannel objChan = e.getChannel();
    	User objUser = e.getAuthor();
    	
    	//in-Bot Komutlar
    	if(objUser.getId().equals(DoctorOneBot.getSelfUser().getId())) {
    		deleteMessage();
    		getMessage(objMsg);
    	}
    	
    	//Spam Kontrol
    	if(!objUser.isBot()) {
    		if(!objChan.getName().equals("pokebot")) {
	    		if(!addedUsers.contains(objUser)) {
	    			addedUsers.add(objUser);
	    			spammerList.add(new SpamCatcher(objMsg));
	    		} 
	    		for(SpamCatcher sc : spammerList) {
	    			if(sc.getUser().equals(objUser)) {
	    				sc.addMessage(objMsg);
	    			}
	    		}
    		}
    		
    	}
    	//Mesaj Parçalama
    	String[] message = objMsg.getContentRaw().toLowerCase().split(" ");
    	ArrayList<String> messageList = new ArrayList<String>();
    	for(String m : message) {
    		messageList.add(m);
    	}
    	//Sohbet kanalında PokeBotu Kapama
    	if(deleteLast2Message) {
    		deleteLast2Message = false;
    		objMsg.delete().queue();
    		
    	}
    	String kanalAdi = "sohbet";
    	if(objMsg.getContentRaw().contains("p!")) {
    		if(objChan.getName().equals(kanalAdi)) {
    			if(objMsg.getContentRaw().contains("p!catch") || objMsg.getContentRaw().contains("p!hint")) {
    				
    			} else {
    				try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
    				//Pokécord
    				objMsg.delete().queue();
    				deleteLast2Message = true;
    				if(!objUser.getName().equals("Pokécord")) {
    					objChan.sendMessage("Bu kanalda bot komutları yasaktır! " + objUser.getAsMention()).queue();
    				}
    			}
    				return;
    		}
    	}
    	
    	/*
    	 * Özel mesajlara kapatma
    	 * 
    	 * */
    	if(e.isFromType(ChannelType.PRIVATE) && !objUser.isBot()) {
    		embed = new EmbedBuilder();
    		embed.setColor(Color.RED);
    		embed.setDescription("Bu bot özel mesajlara kapalıdır.\nÖzelden yazacaksanız, şu hesaba yazınız "
    				+ "<@218356083704463363>");
    		userList.add(objUser);
    		objChan.sendMessage(embed.build()).queue();
    		String s = BotUI.Logger.getText();
    		BotUI.Logger.setText(s + "Bu bot özel mesajlara kapalıdır. \nMesaj : " + objMsg.getContentRaw() + "\nGönderen :" + objUser.getName() + "\n");
    	}
    	//Kullanıcı Komutların Fonksiyonları
    	if(!objUser.isBot()) {
	    	selamAlma(messageList, objChan, objUser);
	    	iyiGeceler(objMsg, objChan, objUser);
	    	gunaydin(objMsg, objChan, objUser);
	    	bilgi(messageList, objChan, objUser);
	    	cekilisYap(messageList, objChan, objUser);
	    	//muzikCal(messageList, objChan, objUser, e);
    	
    	}
    	
    	
    	//Botlara Komutlar
    	if(objUser.isBot() && !objUser.getAsMention().equals(DoctorOneBot.getSelfUser().getAsMention())) {
    		noPokemon(messageList, objMsg, objChan, objUser);
    		getPokemon(messageList, objMsg, objChan, objUser);
    		falsePokemon(messageList, objMsg, objChan, objUser);
    		spawnPokemon(messageList, objMsg, objChan, objUser);
    		levelPokemon(messageList, objMsg, objChan, objUser);
    		hintPokemon(messageList, objMsg, objChan, objUser);
    	}
    }
    /*
     * Komut Methodları
     * */
    
    private void selamAlma(ArrayList<String> messageList,MessageChannel objChan,User objUser) {
    	/*
    	 * Mesaj filtreleri
    	 */
    	String[] selamlar = {"sa","sea","selamunaleyküm","selamun aleyküm","selamlar","selam","s.a","Selamun aleyküm","selamınaleyküm","selamın aleyküm","selamn aleyküm","selamnaleyküm","selamın aleykum"};
    	/*
    	 * Selam alma
    	 */
    	for(String selam : selamlar) {
	    	if(messageList.contains(selam)) {
	    		if(!messageList.contains("aleyküm") && !messageList.contains("aleykum")) {
	    			//System.out.println(DoctorOneBot.getUsersByName("DoctorOne", true)); //<@218356083704463363>
		    		objChan.sendMessage("**Aleyküm Selam** " + objUser.getAsMention()).queue();
		    		String s = BotUI.Logger.getText();
		    		BotUI.Logger.setText(s + "mesaj gonderildi. " + objUser.getName() + " " + objChan + "\n");
		    		System.out.println("mesaj gonderildi. " + objUser.getName() + " " + objChan);
		    		break;
		    	}
	    	}
    	}
    }
    private void iyiGeceler(Message objMsg,MessageChannel objChan,User objUser) {
    	/**
    	 * Filtreler
    	 * */
    	String[] filter = {"iyigeceler","iyi geceler"};
    	
    	for(String s : filter) {
    		if(objMsg.getContentRaw().toLowerCase().contains(s)) {
    			objChan.sendMessage("**İyi Geceler**").queue();
    			//Logger
    			String s1 = BotUI.Logger.getText();
    			BotUI.Logger.setText(s1 + "İyi Geceler - " + objUser.getName()+ "\n");
    			try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
    			break;
    		}
    	}
    }
    private void gunaydin(Message objMsg,MessageChannel objChan,User objUser) {
    	/**
    	 * Filtreler
    	 * */
    	String[] filter = {"günaydın","günaydınlar"};
    	
    	for(String s : filter) {
    		if(objMsg.getContentRaw().toLowerCase().contains(s)) {
    			objChan.sendMessage("**Günaydınlar** " + objUser.getAsMention()).queue();
    			//Logger
    			String s1 = BotUI.Logger.getText();
    			BotUI.Logger.setText(s1 + "Günaydın - " + objUser.getName()+ "\n");
    			try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
    			break;
    		}
    	}
    }
    private void bilgi(ArrayList<String> messageList,MessageChannel objChan,User objUser) {
    	/**
    	 * Filtreler
    	 * */
    	String[] filter = {"botbilgi","bilgibot","botbilgiver","botkomut","botdiğerkomut","botkomutları"};
    	
    	for(String s : filter) {
    		if(messageList.contains(s)) {
    			embed = new EmbedBuilder();
    			embed.setAuthor("DoctorOne Bot","https://www.youtube.com/mahmutkocas","https://cdn.discordapp.com/avatars/473661328691822592/c2b0abf6e1a65d2ee0f63c8c2cb7cb11.png");
    			embed.setColor(Color.MAGENTA);
    			embed.setTitle("Bot Komutları","https://www.youtube.com/mahmutkocas");
    			embed.setDescription("Bu bot <@218356083704463363> tarafından,\nSelam almak amaçlı yapılmıştır.\nDiğer özellikleri süstür.");
    			embed.addField("Komutlar", "botbilgi\t\t-- Bot Hakkında Bilgilendirme\n"
    					+ "çekilişyap <katılımcılar>\t-- Çekiliş yapar.\n"
    					+ "günaydın\t\t-- Günaydın'der.\n"
    					+ "iyi geceler\t\t-- İyi Geceler'der.\n", false);
    			embed.addField("Özellikler","Pokemon Botunun bir kaç özelliğini Türkçeye çevirir.\n"
    					+ "Spam Engeller.",true);
    			embed.addField("Sosyal Medya","[Youtube Kanalımız](https://www.youtube.com/mahmutkocas)",true);
    			objChan.sendMessage(embed.build()).queue();
    			//Logger
    			String s1 = BotUI.Logger.getText();
    			BotUI.Logger.setText(s1 + "Bilgi Aldı --> " + objUser.getName()+ "\n");
    			break;
    		}
    	}
    }
    private void cekilisYap(ArrayList<String> messageList,MessageChannel objChan,User objUser) {
    	/*
    	 * Filtreler
    	 * ve
    	 * Random Değeri
    	 * */
    	String[] filter = {"çekiliş","çekilişyap"};
    	Random rand = new Random();
    	
    	
	   	for(String s : filter) {
	   		
	    		if(messageList.contains(s)) {
	    			messageList.remove(s);
	    			if(messageList.contains("yap")) {
	    				messageList.remove("yap");
	    			}
	    			if(messageList.size()-1 == 0){															 //Az kişi
	    				objChan.sendMessage("```css\n"
	    						+ "Çekiliş için en az 2 kişi girin."
	    						+ "```").queue();
	    				//Logger
		    			String s1 = BotUI.Logger.getText();
		    			BotUI.Logger.setText(s1 + "Çekiliş Komutu az kişi -> " + objUser.getName()+ "\n");
	    			} else if(cekilisFarkli(messageList)) {
	    				objChan.sendMessage("```css\n"
	    						+ "Birden farklı katılımcı gir. Çakal seni."
	    						+ "```").queue();
	    				//Logger
		    			String s1 = BotUI.Logger.getText();
		    			BotUI.Logger.setText(s1 + "Çekiliş Komutu aynı kişi -> " + objUser.getName()+ "\n");
	    			} else if(messageList.size()-1 > 0) {													 //Kazanan
	    				int kazanan = rand.nextInt(messageList.size());
	    				
	    				//Kazananın İlk Harfi Büyük Yapma
	    				String capital = messageList.get(kazanan).substring(0, 1).toUpperCase();
	    				String rest = messageList.get(kazanan).substring(1);
	    				capital = capital + rest;
	    				
	    				//Embed Build
	    				embed = new EmbedBuilder();
	    				embed.setDescription("Kazanan " + capital + "!!!");
	    				embed.setColor(Color.GREEN);
	    				
	    				objChan.sendMessage(embed.build()).queue();
	    				//Logger
	    				String s1 = BotUI.Logger.getText();
	    				BotUI.Logger.setText(s1 + "Katılımcılar : " + messageList.toString() + "\n->Kazanan:  "
	    						+ "" + messageList.get(kazanan) + "\n-->Çekilişi Yapan :"
	    								+ "" + objUser.getName()+ "\n");		    		
	    				} else { 																			//Katılımcısız
			   			objChan.sendMessage("```css\nKatılımcıları girmediniz.\nKomut Kullanımı\nçekilişyap <katılımcılar>```").queue();
		    			//Logger
		    			String s1 = BotUI.Logger.getText();
		    			BotUI.Logger.setText(s1 + "Çekiliş Komutu Yanlış Kullanımı -> " + objUser.getName()+ "\n");
			   		}
	    			break;
	    		}
	   			
	   	}
    }
    private void duyuruGonder(ArrayList<String> messageList,MessageChannel objChan,User objUser) {
    	/*
    	 * Filtreler
    	 * */
    	String filter = "duyuru";
    	
    	if(messageList.contains(filter)) {
    		messageList.remove(filter);
    		String URL = messageList.get(0);
    		
    		
    		
    	}
    }
    /*private void muzikCal(ArrayList<String> messageList,MessageChannel objChan,User objUser,MessageReceivedEvent e) {
    	/*
    	 * Filtreler
    	 * */
/*    	String[] filter = {"oynat","cal"};
    	for(String s : filter) {
    		if(messageList.get(0).equals(s)) {
    			messageList.remove(s);
    			String URL = messageList.get(0);
    			
    			Guild guild = e.getGuild();
    			Member member = e.getMember();
    			VoiceChannel voiceChannel = member.getVoiceState().getChannel();
    			AudioManager audioManager = guild.getAudioManager();
    			AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
    			playerManager.registerSourceManager(new YoutubeAudioSourceManager());
    			
    			final AudioPlayer player = playerManager.createPlayer();
    			playerManager.loadItemOrdered("https://www.youtube.com/watch?v=2cjbSgy3vSw",URL, new AudioLoadResultHandler() {
					
					public void trackLoaded(AudioTrack track) {
						System.out.println("çalıyor.");
						player.startTrack(track, true);
					}
					
					public void playlistLoaded(AudioPlaylist playlist) {
						System.out.println("playlist ?");
						
					}
					
					public void noMatches() {
						System.out.println("bulunamadı");
						
					}
					
					public void loadFailed(FriendlyException exception) {
						System.out.println("yüklenemedi");
						
					}
				});
    			//audioManager.setSendingHandler(new SimpleAudioHandler());
    			audioManager.openAudioConnection(voiceChannel);
    			System.out.println(URL);
    			System.out.println("\ntest");
    		}
    	}
    }*/
    /*
     * Botlara Komutlar
     * */
    private void noPokemon(ArrayList<String> messageList,Message objMsg,MessageChannel objChan,User objUser) {
    	
    	/*
    	 * Filtreler
    	 * */
    	String filter = "There's no wild pokemon in this channel!";
    	
    	if(objMsg.getContentRaw().equals(filter)) {
    		objMsg.delete().queue();
    		objChan.sendMessage("```css\n"
    				+ "Yabani Bir Pokemon Bulunamadı! Sonra tekrar gel!"
    				+ "```").queue();
    		//Logger
			String s1 = BotUI.Logger.getText();
			BotUI.Logger.setText(s1 + "Pokemon Bulunamadı - " + objUser.getName()+ "\n");
    	}
    }
    private void getPokemon(ArrayList<String> messageList,Message objMsg,MessageChannel objChan,User objUser) {
    	
    	/*
    	 * Filtreler
    	 * */
    	String filter = "You caught a level";
    	
    	if(objMsg.getContentRaw().contains(filter)) {
    		objMsg.delete().queue();
    		String[] mL = objMsg.getContentRaw().split(" ");
    		//Required Infos
    		String yakalayan = messageList.get(1).substring(2, messageList.get(1).length()-2);
    		User catcher = DoctorOneBot.getUserById(yakalayan);
    		
    		String level = messageList.get(6);
    		String pokemon = mL[7].substring(0, mL[7].length()-1);
    		
    		//Embed Build
    		embed = new EmbedBuilder();
    		embed.setColor(Color.CYAN);
    		embed.setTitle(objMsg.getGuild().getMember(catcher).getEffectiveName() + " Pokemon Yakaladın!");
    		embed.setThumbnail(LastPokemonURL);
    		embed.setDescription(level + " Level " + pokemon + " yakaladın! **Hayırlı Olsun!**");
    		
    		objChan.sendMessage(embed.build()).queue();
    		deleteOK = true;
    		//Logger
			String s1 = BotUI.Logger.getText();
			BotUI.Logger.setText(s1 + "Pokemon Yakalandı - " + level + "." + pokemon + " " + yakalayan + "\n");
    	}
    }
    private void falsePokemon(ArrayList<String> messageList,Message objMsg,MessageChannel objChan,User objUser) {
    	
    	/*
    	 * Filtreler
    	 * */
    	String filter = "This is the wrong pokémon!";
    	
    	if(objMsg.getContentRaw().contains(filter)) {
    		objMsg.delete().queue();
    		objChan.sendMessage("```css\n"
    				+ "Pokemon adı yanlış!"
    				+ "```").queue();
    		
    		getSpawnMessage = true;
    		//Logger
			String s1 = BotUI.Logger.getText();
			BotUI.Logger.setText(s1 + "Yanlış Pokemon Adı. - " + objChan.getName()+ "\n");
    	}
    }
    private void spawnPokemon(ArrayList<String> messageList,Message objMsg,MessageChannel objChan,User objUser) {
    	
    	/*
    	 * Filtreler
    	 * */
    	String filter = "‌‌A wild pokémon has appeared!";
    	MessageEmbed em;
    	if(!objMsg.getEmbeds().isEmpty()) {
    		em = objMsg.getEmbeds().get(0);
	    	if(em.getTitle().contains(filter)) {
	    		
	    		LastPokemonURL = em.getImage().getUrl();
	    		ImageIcon icon = new ImageIcon(LastPokemonURL);
	    		
	    		//Embed Building
	    		embed = new EmbedBuilder();
	    		embed.setColor(Color.RED);
	    		embed.setTitle("Yabani Pokemon Belirdi!");
	    		embed.setDescription("Yakalamak için 'p!catch <pokémon adı>' yazın!");
	    		embed.setImage(LastPokemonURL);
	    		objChan.sendMessage(embed.build()).queue();
	    		getSpawnMessage = true;
	    		
	    		
	    		//Logger
				String s1 = BotUI.Logger.getText();
				BotUI.Logger.setText(s1 + LastPokemonURL + "\n");
				
				try {
					Thread.sleep(2000);
					objMsg.delete().queue();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    	}}
    }
    private void levelPokemon(ArrayList<String> messageList,Message objMsg,MessageChannel objChan,User objUser) {
    	/*
    	 * Filtreler
    	 * */
    	String gidecekKanal = "pokebot";
    	String filter = " is now level ";
    	MessageEmbed em;
    	if(!objMsg.getEmbeds().isEmpty()) {
    		em = objMsg.getEmbeds().get(0);
	    	if(em.getDescription().contains(filter)) {
	    		//Embed Building
	    		embed = new EmbedBuilder();
	    		embed.setColor(Color.BLUE);
	    		embed.setTitle(em.getTitle().split(" ")[1] + " Pokemonun level atladı!");
	    		embed.setDescription(em.getDescription().split(" ")[1] + " " + em.getDescription().split(" ")[5].substring(0, em.getDescription().split(" ")[5].length()-1) +" Level Oldu!");
	    		objMsg.getGuild().getTextChannelsByName(gidecekKanal, true).get(0).sendMessage(embed.build()).queue();
	    		objMsg.delete().queue();
	    		//Logger
				String s1 = BotUI.Logger.getText();
				BotUI.Logger.setText(s1 + "Pokemon Level Atladı" + "\n");
	    	}}
    }
    private void hintPokemon(ArrayList<String> messageList,Message objMsg,MessageChannel objChan,User objUser) {
    	/*
    	 * Filtreler
    	 * */
    	String filter= "The pokemon name starts with";
    	
    	if(objMsg.getContentRaw().contains(filter)) {
    		objMsg.delete().queue();
    		String letter = messageList.get(5).substring(1, messageList.get(5).length()-2);
    		objChan.sendMessage("```ml\n"
    				+ "Pokemonun ilk harfi : '" + letter.toUpperCase() + "'!"
    				+ "```").queue();
    	}
    }

    /*
     * Fonksiyonel Metodlar
     * */
    private boolean cekilisFarkli(ArrayList<String> messageList) {
    	int length = messageList.size();
    	int count = 0;
    	for(String s1 : messageList) {
    		for(String s2 : messageList) {
    			if(s1.equals(s2)) {
    				count++;
    				if(count == length*length)
    					return true;
    			} else
    				return false;
    		}
    	}
    	return false;
    }
    
}
