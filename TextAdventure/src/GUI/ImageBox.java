/**
 * Group Project II: a Text Adventure
 * @author Tex Bishop, Ian Wickham, Jacob Holzmann
 * @version 1.0
 * @due 05-01-2019
 */

package GUI;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.JTextArea;
import java.awt.Font;
import java.awt.Point;

//=====================================================================
//The panel containing the ASCII art image in our application
//=====================================================================
@SuppressWarnings("serial")
public class ImageBox extends JPanel 
{
	private FrameDragListener frameDragListener;
	private JTextArea textArea;
	
	/**
	 * Default constructor. Create the ImageBox panel.
	 */
	public ImageBox(JFrame frame) 
	{
    	//===============================================================
		//Set the parameters for this panel
    	//===============================================================
		this.setBorder(new LineBorder(new Color(0, 0, 0), 4));
		this.setBackground(new Color(255, 255, 250));
		this.setBounds(0, 26, 800, 500);
		this.setLayout(null);

    	//===============================================================
		//The text area containing the ASCII art being displayed.
    	//===============================================================
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setFont(new Font("Monospaced", Font.BOLD, 8));
		textArea.setBackground(new Color(255, 255, 250));
		textArea.setBounds(5, 5, 790, 490);
		textArea.setEditable(false);
		textArea.setHighlighter(null);
		this.add(textArea);

    	//===============================================================
		//FrameDragListener object initialization, to make the ASCII image 
		//area capable of click-dragging the application window
    	//===============================================================
		this.frameDragListener = new FrameDragListener(frame, new Point(5, 26));
		textArea.addMouseListener(frameDragListener);
		textArea.addMouseMotionListener(frameDragListener);
		
		textArea.setText(whiteHouseImage);
	}
	
	public void setImage(String image)
	{
		textArea.setText(image);
	}
	
	//=================================================================================
	//Beyond this point, declaration of final Strings representing the ASCII art text
	//string images used in our textArea object.
	//
	//Parameters for the ASCII art:
	//   Base image needs to be 800 x 460 pixels in size
	//   Use converter at https://www.text-image.com/convert/ascii.html
	//   Convert at image width 157, black text, white background, do not
	//   invert image or add extra contrast.
	//=================================================================================

	private final String whiteHouseImage = "......---------------............````````````                 ````````````````````````````             ``.`` `` .`.``   `.-+syyyosyo+yhhyosyhhmhyyyhhyyhyhhhh\r\n" + 
			"..........--------.................``````````````           ``````````````````````````````````      ```:++--`./-:o:-+`````-:syhsssysoso+/syydddhdddhddhhhddhy\r\n" + 
			"....................................``````````````````````` `````````````````````````````````     ``.-:-+ss+:+s+:+so:.:```-oyhhyhyhhyyooosyyyddhdhdddyyhdhhhy\r\n" + 
			"````````  `````````````` ````````...```````````````````````````````````````````````````````        `-oys+sdy/syhsshyo+/:.:+sssyyhhyhhyysysydhhhdhhhhhhhhdyhhd\r\n" + 
			"``````       ````````         `````````````````````````````````````````````````````````   ````    `.:+yyyshhhhhhshhh+o-::oosyyyyyhhhhyhyhhdmdhyhyyyhddhhhhhyh\r\n" + 
			"`               `````           `````````  ``````````````````````````````````````````   `.::/:.``--/ooyyyhhdhhdhhhdysss+oyoyhyyhhhhhhyyyhhhdhhhhhhyyhhhhhdhhm\r\n" + 
			"`                                `````````` ````````````````````````````````````--``  `.-/oso+-//syyoysoyhdddhhhhyhys++so/oshhhhhddhdyyydddhhhydddddhdhyhmhdm\r\n" + 
			"```       `         ```````` ` `````````````` ```````````````````````````````.``/y+`..`-/+oyyyhhhhyyyssyyhdddddhhydddshysysyhhhhyhhhhhdhhhdhdhshddhdmhddhhymN\r\n" + 
			"`````   `````         `````````` ```````````````````````````````````.:///.`./o+/+yho/s//ssyyyyhhhyhhyhyhhhddhhyddyhdhyhyhsosyhyhdhoohydddhhhdddhdhddhdddyhydh\r\n" + 
			"``````   ``````    ```   ````````````````````````````````:-```` ``::+oyhy//:o+:--:-:+yyyyyyyhyhddhyhyyyhhhyhhhdhhhhdddddyyyhyyhhysyyhhdhdhdhhyoydyhhydmNdmddh\r\n" + 
			"```...```` ```````````   ````````````````````````````````..``````:/oso+o/:..`    `://+yhyyyhdhhyyyhsosssyyyhyyhdmmmdddmhhhssssshhyyyhyyyyhdhyyhhhddhhhdhdmmmm\r\n" + 
			"`````````.```````````` `.````````````````````````````````.```..-.-..`           -/:/:/+yhyshhhhhdddhhhyyhdhhhhddmdhhdmdyhyyyyyysyhhhdhhyhddshdddhhhdhddhhmmNd\r\n" + 
			"```````````````````````...`````.``.......````...`````````.`                   .::://:///yhhyyhhdhdddhhhddmdmddhdsyhdddddhsyyyyyyyyhhhhhhddddhyhmmmdhddmmydmmd\r\n" + 
			"``````````````````````.---.```````````````..``........`                     `::://///:::/ohyhhyhyhhhyyyyyhddhdhddddmmmmmdyyyyyyyysyhyyhddhddyhyyhdshhsymdmmNm\r\n" + 
			"````````````````.......-::-.```````````````````````````                    -/:::::::::::::ohyohhhhyhyyyhyyhhhhddddhhhddyyysyhhhyydyhyhyshdyddhddhdh+s-ymmmmNm\r\n" + 
			"```````````..```-::-..-::::-..````````````````````````                   ./::::::::::::::::+syyyyhhhyyyhhddhhdmdddddmdhhhdhdyyhhhyhddddhhmddmmdhhmmmdosmhdmmm\r\n" + 
			"````.....-+s+::+sss/.-::::-.-..``````````````````````                  `::::::::::::::::::::/shyyhdddyydmddhdmNmmddddmNmddhyyydyyhhmdhohddNmNdhydhdhmNmmddydm\r\n" + 
			"//++ssso+sysysssyy/.-::::-`..-..````````````````````                 `-/:::::::::::::::::::://shhddhhsyhyhhhhdmmNNmhhhyossyyssyysyhhyyyhhhmdmNmddmmmNNmmdddmm\r\n" + 
			"yssyhyysossyyysyy+---:::-`````-..```````````````````                ./::::::::::::::::::::::://oydhshhdyhdmmmmmmmmhhyymyysyysssyyyhyyyhhhymmdmmdmmmNmmNNmdddh\r\n" + 
			"yhhyyhyysyhhysyyo-.-:::++:sso.....`````````````````               `:::::::::::+mddsshyy/:::::://ohhdhyddhdmmmmmhdmdssoydyyhhhshhhohyyhhhddmddmmhyhmmNNmdddddh\r\n" + 
			"yhyhysyyyhhyyyys-.-:::/dd+dds.....-.```````````````          ````./:::::::::::+ddmyymmm/::::::://+hddddddhhddmdmmdmdsyysyydmmhmmmyysshhhdyyohhhdmmdmNddhdhhhd\r\n" + 
			"hhhyyyhhhhyyyyy-.-:::-:hy/ys+.```.................................-:::::::::::/yyhssdyd/::/:/:::///syhhhyddhhhmmmmmNmddhydddmmddoyhsdmdhhyyoshhdhhdmhmNmNhydh\r\n" + 
			"dddhhyhhyyyhhh/.-::--.:yy/hh+`````............----:::::----...-...-:::::::::::+mmmyyNNm//////:///++odmddhyssdddmmmmmNNNmyysyddyy+/hdmNdyhdyyhhyddddmmNNNNdooy\r\n" + 
			"ddhhysyyyyhhyyyys::-..:hh:hh+``````......-..-----:::/::----.......-::::::::://+mmmyyNNm//////////hdhhhddsssymddmdmmmmmNNmmdmhshs+/soyhyhhhsshdhmdmdmNNNNMdmmh\r\n" + 
			"hhyssyyyyyyysssys--.``-//.:-.``````...----------:::///:------..`.`-::://///////+++++oos//////////oyysoo+/-/odddmNNmmdmdNNNdhyddyhhyhdddmddsydmmdddsdddmhNdsyy\r\n" + 
			"dhhyysyyysyyyyyys-.````````````````..-------::::/:::--.```````````-/++///////////////////////////sso+soys+ssyhhhhmdmddmdhyyhhdmmdhdhhdddyyhhdhdyysddhhdmmy-:+\r\n" + 
			"dhhyyyyyyyyyyhhys.`````````````````.--::::::-..`````````````        `..-::///++++++oooooo+++++++ooo//+/o+++sdyyhymhyyyyyyyyyyyhhhhmmdyoyhyydhhdyyhddddhNNd/+s\r\n" + 
			"dhhyyyysoo//:-.````````````````````.......-----::::::://::----------............`.......-----::::::::::-`   `..-:+/+ossyyssyhdddydNNmmdydhhdsshsddddhhodmNmdd\r\n" + 
			"+/:--....-.-------::::::::////////+++///////////++++++++++++////////////////////////////////////////////////////::::::://+hdNmNNNMMMMNmdNNmddddmmmNmmmddNMMNN\r\n" + 
			":::::::://///////+++++++++++++++++++++++++++++++++oooooooooo+////////////////////////////////+++++++++++++++++++++++ymmmNNNNNNmNNMMNMNNNNNmddmmmmmmNNNNNNMMNN\r\n" + 
			"/osos/:///////////////////////////////////////+++ooooooooooo+/oossssssssssssssdNmNy+ommysssssssssssssoossssssddmmddsoNNNNmmmNNNMMMMNNNNNNmmhdmmmmmNNNNNNNMNNm\r\n" + 
			"/dmmh:::::://///+ss+//////////////+sss+ssso///+++oooooooooys+/hshhhhhssssssdhsdNNNy+sNNysyhysssydddyyosssssssdmmmdNsoNmNNNmmmNNNNMNNmmMNMNmhmmdmmdNNNmdmNNNdy\r\n" + 
			"/NNNd:::::::::::+dm+///////://////+ddmommdy///++++oooooooods+/NymNNNmsssssssssdNNNy+sNNysssssssyNmmhhosssssssdmdmmNsommNNNNNNMMNMMMNNNNMMNNdmNmmmmmmNmmmNNNmd\r\n" + 
			"/Nmmh```........:yy-``````````....:dmN/mmdo--.+++++ooooooods//NsmNNNmoooosssssdNNNhosNNyssssssssmmmyhosssssssdmNNNNyomNNNNNNMMMMMMMMMMMMMMNdmNmdmdmdmNmmNMNMN\r\n" + 
			"+ddds````````...:yh-..........````:syy:yyy/..`+++++++oooooho//dsddmmhooooooooodNNNhosNNyssssssssNNNydssssssssdNMMNMyoNNNmddmNNNNMMMMMMNMMMMdmNNMNdmdmNNNNMMMM\r\n" + 
			"/ooo/```............``````........:ddd:dmdo...++++++++oooods//NsmNNNdooooooooshNmNhosmNhsssssssyNNNhdsssydhhsdNNNmNyoNNNmmmNNNNNNMNNNMNMMMNddmmMNNNNNNMMNNNNN\r\n" + 
			"::--.``....``````.........````..``:hdd:dmd+..`++++++oooooods++NymNNNmoooossssshNNNhssNNhssssssyyhmdyysoyyhdmydmmmmmysdhhhyhhhhhhhhhdddNNmdddhdmmmdmmmmNNmmmmm\r\n" + 
			"oo:-.``.````.....````````........`-///-+oo:...+++++oooooosho++mmdddyysssssssssdNNNdssNNhssssssyyyhhhyysyhdhdhhdssooysooosssssysooooooossyyyyydhdddddddddmmmmm\r\n" + 
			"--/ooo++/::-..````````...````.``............``++++oooooossyo++dmddhhssyssssssshNNNhssmmdyyyhhhhhddddhyshhhhdhhddddddddhhhhhhyhyyyysssoossssooooooosyyyhyhhdhy\r\n" + 
			"+++++++oosssssysssso+++//:-..`````````````````+++oooooossyyo++dmdddmdddhhhhhhhhdddhsshhhhhhhhhhhddddmmmmmmmmmmmmdddhhyyyyssssosssoosooososssososooooooossoo++\r\n" + 
			"+o++++++++++++++++ooossosssyyyyyyysoo++/::-...+osyyyyyyhhhho+oddddhhhyhhhhhhhhhddddddmmmmmmmmmmmmdddhhhyyyssssoooooooooooooooooooooooooooooooooo+++/::--.....\r\n" + 
			"oooooooooooooo+ooo++++++++///+++oooooossssyyyyddhddhhoossssosyhdddddmmmmmmmmmmmmmdddddhhhyyssssooooooooooooooooooooooooooooooooooo+o++///:---................\r\n" + 
			"ooooooossosoooooosoooosooooooo+ooo++++++++++++++o+oo++oooooosyyyhhdddddddhyyyyssssooooooooooooooooooooooooooooooooooo+ooo+++///::--..........................\r\n" + 
			"sssososoosssoossooosoosooooooooooooooooooooooooooooooo++ooooooooooooooooooooooooooosoooooooooooooooooo++oo++ooo+++//:::-.....................................\r\n" + 
			"ssssosoososssossosooooooosoosooooooosoooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo++///::--......:.......................--.-.............";
	
	private final String portrait = "yyhhhhdddddddddddddmmddmmmdddhhddhhhhyyssoooosooooossoo+++/:::--:/sysshmNNNNNNNNNNNNNNNNNNNNNNmmmddddhhhddmmmNmmdhyyyyyyyyyyyyyyyyyyyyyyyhhhhhhhhdddddddddddd\r\n" + 
			"ssyhddddddddddddddddddddddddhhhhhhhhyssoooooosoo+ooooo+++///::--:/shmNNNNNmNNNNNmmNNNNNNNNNNmmmmmmmdhdhhyyhhmNmmmdhyyyyyyyyyyyyyyyyyyyyyyyhhhhhhhhhddhhhddhdd\r\n" + 
			"syyhhdddddddddddddddddddddhhhhhhhhhhyyysssssssoo++++oo++++//:::/oymNNNNNNNmmmmmmmNNNNNNNNNNNmmmmmdmddhhhyysyhNNNmdhyyyyyyssssssyyyyyyyyyyyyyhyhhhhhhhhhhhdddd\r\n" + 
			"yyyyyhhhhdddddddddddhhhhhhhhyyhhhyyyyyyyssoooo++++++++++////oyhmmmNNNNNNmNNNNmmmmNNNNNNNmmmmdhhhhhddddhhhhyyydNNmmmhyyyysssosssyyyyyyyyyyyyyyyyhhhhhhhhhhhhhd\r\n" + 
			"yyyyyyhhhhhdddddddddhhhhddhhhyhhhyyysyyyssooo+++++////+/////yhdNmNNNNNNNNNNNmmmmNNNNNmmddhyso+++//++ossssssoosdmNmmdhyssssoooosssssssssssyyyyyyyhhhhhhhhhhhhh\r\n" + 
			"yyyyyyyhhhhddddddddddhhhhhhhhhhhhyyssssysso++++++//////////ohmNmdmNmNNNNNNNNNNNNNNNmdddhsso++//::--::::::::::+ydmmmNdyysssooooooooosssssssyyyyyyyyyyyyyyhhhhh\r\n" + 
			"syyyyyyhhhhddddddddddhhhhhhhhhhhyyyyyssssoo++++++/////////sohmmmdmNNmmNNNNNNNNNmmmdhhysoo++///::-----.......-:+yddhymmysooooooooooooossssssyyyyyyyyyyyyyyyhhh\r\n" + 
			"ssssyyyhhdddhhhhhhhhhhhhhyyyyyysyyyyssooooooo++++/////////osymmmNNNNNmmmddhhhhhhyyyyyso++++//::-----.........-:/sddhshdyo++o+o+ooooooosssssyyyyyyyyyyyyhhhhhh\r\n" + 
			"ssssyyhhhhhhhhhyyhhhhhhyyyyyysoosyyssoo++//+++++////++++++oshmNNNNNNNmmddhyyssssyyyhys++/////::----...........-:+hmdhyyhs++++oooooooosssssssssyyyyyyyyhhhhhhh\r\n" + 
			"osssyyhhhhhhhhyyyyhhyyysssssssoosyyso+++/////++++++++++++ooodmNNNNNNmmdhyyyyyhhdhhhhyo+////:::-----...........--/sdmddhys++++++oooooooosoossssyyyyyyyyyhhhhhh\r\n" + 
			"ooossyyhhhhhyyyyyyyhyyssoooooooossso++///+/+//+++++++/++++yhmmmmmdmmmmddddmNNddhhhhysooo+++/:----.............--/sdmmmdys++/+//+o+oooooooooossssyyyyyyyyyhhhh\r\n" + 
			"+ooossyyyhhyyyyssyyyysssssoooooooso+++++/++++++++/+/////+ohdmmmmNNNmmmmNNMMNddhhysooo++/:::+o+:---............--:oddmmmds++/////+++ooooooooosssssssyyyyyyyyhh\r\n" + 
			"oooooossyyyyyyyyyyyysssssssssssssoo+//////+++o++//////+++oyddmNNNmmNNNNNNNmdhhysooosyddhhyso//+:---............-:oddmmmdy+/////++++++ooooooooossssssssyyyyyyh\r\n" + 
			"sooooooosssyyyyyyyyyysssssssyyssooo+////+++o++s+//++//+++ohdmmmNNmNNNNdhmmdhhhysoooshhhhy+-os/++:-...-:///::-..-:sdmmmmdho+//////+++++++++oooooosssssssssssyy\r\n" + 
			"ooooooooossssssyyyyhyyssooossssoo++////++ooo+++sso+++//+sydmNNNNmmmNNhhydmhhhyyso+////++////++ys/-...-:///::::::ohdmmmmdyy+////++++++++++++oooooossoossssssss\r\n" + 
			"+oooooooosyyyssyysoossssoooooooo+++////++++++++++syyssyhdmmmmNmNmmmNNhshddhhhyso+/:--------:/oso/-..-+ohhys/:..:ddmdddddyyo//////+++++++++oooooooooooosssssss\r\n" + 
			"+oooooosyyyyyssyyo+++oo++oooooo+++++////++/////++//+++ohyhmmmmmNdmmNNmdmdhhyysoo+/:------://++o+:.`.-/oso/:yo:-sdddmddddyys+//:///+++++++ooooooosssssssssssyy\r\n" + 
			"++ooooosyyyyssssyssooooooooooo++++ooo++///////+++ooooshhdmmmmddmhmmNNMMmdhhysso+//::----:/++oo+/-....-:://+/:-/mdmddmhdhyhs+/+/://+++++++oossssssssssssssyyyy\r\n" + 
			"++++ossyyssssssssssysssssso++++++oso++//////yyyhyyssyhyhddddhymmhmNNMMMdhyyssoo+//:-----:+sso+/:......---...--ymmdmmmddhyyhyo/o+//////++++oossooossssssssyyyy\r\n" + 
			"++++osyyyssssssssssyyssssoo++o++ooo++///////+hmmmmmmdhhdddhyhdmNhmNNNNNdyyssso++//::---:/so+++/-.``........--+mmNNmmmNmdhyhyys//+//////+++oossoooossssssssyyy\r\n" + 
			"++++oosssssssoosssssssssoooooo++++o++///////+dmmmmmmmmmmdhydmNNdhmNNNNNhysssoo++////:::/++++o+/-.........---:hddNNNNmmmmdhyyhdyo////////++ooooooosssssssssyyy\r\n" + 
			"oo+++ooossyssoooossssoooooossoo++oo++//++++++sNNmmmmmmmdddmNmNhhdmNNNNNdysoooo++++//://///+/::-.-......----:dmdmNNNNNmddhhsyydmmdyo+:::/++ooooooooooossssssss\r\n" + 
			"o++++oossssssooossssssssoooosoooo++////++++++odmdmmmmmdmNNNmdshmmmmmmmNmysoooo++//::+++///::...`.......---/dNmdmNNNNNmddhyo/shyhddyyo+++++ooooo+++oooooosssss\r\n" + 
			"o+++oossssssssossyyysssssssssoooo+/////+++++++oyhhyhmNNNNNdhyhdddddmdmNdysoo++++////+osoo+++/:-.......---+mNNNmNNNNNNmmdhsosddhs+oyoo+++++ooooo+++ooooooossss\r\n" + 
			"oooooosssssssysssyyyyssssssssssso++////+++++o+++osmNNNNNmhhdhddhdddddNNdyssoooo+++////++:----:://-...--:ymmmmmmmmNNNNNNmdoymmmdhyo+o/+++++oooooo+ooooosssssss\r\n" + 
			"+++++oossssssssssssssssssssssssoo++/+/////+++++oydNNNNmddhdhdhymmmddNNmhysssoooo+++/::::::--....------+dmNmmmmmmdmNNNNNmdshdmmdydho++/+++++ooooooosssssssyyss\r\n" + 
			"+++ooosyyyyyyyyssssssssssyyyssooo++///////++oshmmmNmmmddhhhyssdmmdhmNNmhyssssooo+++/:----.......----+hmmmmNmmmmmhdNNmdhdhhhmhhhyy+//+////+++ooosssssssssssoo+\r\n" + 
			"o+++oosyyyyhhyysssssssssyyyyyysoo+++++++++++shdNmmmmmdhsohyosmddddmmmmdhsooooo++++//:--...```..--:sdmmmdmmNmdmmmydmdhhdmdsdhyyyo/////+/://+ooosssssssssoo++//\r\n" + 
			"o++++ossyyyyysoosssssssssssyyyssooooo+oooooshdmmmNNdhyoyysoymmmdmmdhNmdysooo++//////:---......-:sNNNNmmmNmmmddddsmdhsddddhdssy+:////:/+://+oooossssssso++//::\r\n" + 
			"oo++ooooooosoooooossssoooosssssoosoo++oymNNNNNNmmhysyhhsoshNNmmmmo+smmdyoo++++/////////::-----:/mmmmNNmmNNdhhhhhydhoyyodhhdys+:///////o//++oooosssssoo+///:::\r\n" + 
			"ssoooosoooosssssssssssssooosssoooos+//sdmmmmmdhhhhdhyoosydNNNNNdo/+odmdyo+++++//////////::----:oddddmNNmNmddhshhmdssy/+yhydyo+oo++///oo/++++++oossoo+//:::---\r\n" + 
			"yyyyyyyyyyyyysssssssssssssssssooo+osohhmmmmmmmmdhysosyhmNmNNNNmy+++hNmdyo+///////////::------:oddhydNNMNNmmmmNNdmhhyoohyhoddho+++o+/o+++++++ooooo++//:::-----\r\n" + 
			"yyyyyyyyyyyyyysossssssyyyssssssoo+++sdmmNNNmmdhhysoodmmmdmNNNmmo+sdNNmdyo///////////::------:odmhymhhmNNNmmNNNNmmhhssoyo+:ydmmdyo+//++++++ooooo++///:::------\r\n" + 
			"sssssssssyyyyyysssssssssssossooo+///oyhmmhyo+/::-::/+ymmmmNNNNdhmmNNmddy+/::///////::::---::+hddyhNdhdddmmdhhhhdmdyso+s-./+oyhyyysoo+++ooooo++//:::::----::::\r\n" + 
			"ooooooosssyyyysssoooo+ooooooooo+++/+ohhhs+//++//++o+++sdmNNMMNmmNmmmdhdy/:-:///++///:::::::/shhhhdmhhhdddhysshddddds++:/:///+os+osssss++ooo+/:-----...------:\r\n" + 
			"ssoooossssyyyysoooo+o+ooooooossoo+++yhso+/////-------:::dNNNmmmmmmddhdds:--://+///:::::::://+sydmmmmmdhysooo++shdhdyoo+ossoo////::/oo+/++++/:--........---:::\r\n" + 
			"ssoooossossyyssoooooo+++ooooosso++oyso+++//::::::://////sNMNmddddhhhyydo:--/oo+//::::::::/+sydmNNdNdhyysshhdmdddddddyhoo+++s+/+////////////:::----....--:::::\r\n" + 
			"sssooosssssyyyssooooooooooooooo+oyyo+////:::::::-------::hNMNdhhyysoosy+::/oso+//:::/osyddddhhmNmmmhyhodNNNNNNmmmmddddhyysso+ossso++//////:::::::------------\r\n" + 
			"yyyysssssssssssssssssooosssssoyhyo+////:::::::::::::///+/hNNNNNmhysoooo++++ooooosyhdmddhhyyyhdNNmNmhdysmmmmmmmmmmdddhhyyhhy++yhyyyyo/:::---------------------\r\n" + 
			"yhhhhyyyyyyssssyyyssssssyyssyhhyo+///::---:::::::----::::+dNNNNNNmmmmdddddmmmmmddddhhhhhhyyyhhmNNNNNmdddmddddmmmddhhyyyyyhdh++osyyyyy------------------------\r\n" + 
			"hhhhyyyyyyyyyyyyyyyssoosysydhso+++/:::------------------::ommmmmmmmmmmmmdddddddddhhhhhhhhhhhyhhdNNMMNNNNmmmmmmmmmmmmddhhyyhddo::+sss+--------------------::::\r\n" + 
			"yhhhhyyyyyyyyyyyysssooosyhdhs++///::------------:/oyssyso/ymmmmmmmmmmmmddddddddddddddddhhhhhhhhdmmmNNNNNNmmmmmNNNmdyyyssooooshy///++:---------------::::::///\r\n" + 
			"yyyhhhhhyyyyyyyyssosssdNNNNmhso+//::--------::+ydNNNNNmhdmmmmmmmmmmmmmmmmmdmddmdddddddddhhhhhhhhdmddmmmNNNNNNNNmdddhyyssooossyhs:::----------------:::::::///\r\n" + 
			"ssyyhhhhhhhhhdddyssssyNMNNNNNNmho/:::--:://oymNNNNNNNNNNNNNmmmmmmmmmmmmmmmmmmmmmmmmmdddddhhhhhhhddhhdmmmmNMNNmdmmddhyyssooossyhh:-::::-----:::--:::--::::////\r\n" + 
			"sssyyyhhddddddmmdhhyyymNNNNNNNNNNmdhyso+yhNNNNNNNNNNNNNNNNNmmmmmmmmmmmmmmmmmmmmmmmmmmddddhhhhhhhhddhhdmmNNMNmdmmdddhhyyssssssyhd/--::::----::::::::::::////++";
}
