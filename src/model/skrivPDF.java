package model;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import komponenter.FetchPassword;
import komponenter.Password;

import userInterface.RecipeWindow;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import databaskomm.SkrivUt;
import databaskomm.Fetch;
import databaskomm.LetaRecept;
import databaskomm.FixaText;

public class skrivPDF implements Ruta {

	static String[] recept;
	static int i = 0;

	public void kor() {
		skriv();
	}
	
	public void dispose(){
	}

	public static void skriv() {
		recept = LetaRecept.Recept("");
		i = 0;
		Document document = new Document(PageSize.A4, 50, 50, 50, 50);
		document.addCreationDate();
		try {
			PdfWriter.getInstance(document, new FileOutputStream(
					"C:\\Users\\Magnus\\Documents\\kokbok.pdf"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		document.open();
		Paragraph titel = new Paragraph("Magnus kokbok", FontFactory.getFont(
				FontFactory.HELVETICA, 30, Font.BOLDITALIC));
		titel.setAlignment(Paragraph.ALIGN_CENTER);
		Chapter title = new Chapter(titel, 0);
		title.setNumberDepth(0);
		Image image;
		try {
			image = Image.getInstance("kokbok.png");
			image.setAlignment(Image.MIDDLE);
			title.add(image);
			document.add(title);
		} catch (Exception e1) {
			e1.printStackTrace();
			System.exit(0);
		}

		Chapter[] chapters = new Chapter[recept.length];
		while (i < recept.length) {
			try {
				Paragraph title1 = new Paragraph(recept[i].replace("  ", ""),
						FontFactory.getFont(FontFactory.HELVETICA, 18,
								Font.ITALIC));
				chapters[i] = new Chapter(title1, i + 1);
				String[][] innehall = SkrivUt.skriv(recept[i], 0);
				PdfPTable t = new PdfPTable(2);
				ArrayList<String[]> tabell = RecipeWindow.fixToTable(SkrivUt.skriv(recept[i], 0));
				
				
				for (int i = 1; i < tabell.size(); i++) {
					t.addCell(tabell.get(i)[0]);
					t.addCell(tabell.get(i)[1]);
				}
				Paragraph line = new Paragraph(" ");
				String[][] fetch = Fetch
						.fetching("SELECT \"Portioner\" FROM \"Bakverk\" WHERE \"BakNamn\" = '"
								+ recept[i] + "'");
				String portioner = fetch[1][0];
				String[][] bild = Fetch
						.fetching("SELECT \"Bild\" FROM \"Bakverk\" WHERE \"BakNamn\" = '"
								+ recept[i] + "'");
				Paragraph port = new Paragraph("Portioner: " + portioner,
						FontFactory.getFont(FontFactory.TIMES, 12));
				if (bild[1][0] != null) {
					Image image2 = Image.getInstance(new URL(
							"http://www.nada.kth.se/~magnlun/Bilder/"
									+ bild[1][0]));
					Chunk kul = new Chunk(image2, 260, -40);
					port.add(kul);
				}
				chapters[i].add(port);
				chapters[i].add(line);
				chapters[i].add(line);
				chapters[i].add(line);
				chapters[i].add(t);
				String temp2 = FixaText.fixaTexten(innehall[1][4].replace("  ",""));
				String ut = "\n";
				for (int i = 0; i < temp2.length() - 1; i++) {
					if (temp2.charAt(i) == '\n') {
						if (temp2.charAt(i + 1) == '\n')
							continue;
					}
					char a = temp2.charAt(i + 1);
					if ((temp2.charAt(i) == '\n' && ((a >= 'a' && a <= 'z')
							|| a == 'å' || a == 'ä' || a == 'ö')))
						ut += " ";
					else {
						ut += temp2.charAt(i);
					}
				}
				if (temp2.charAt(temp2.length() - 1) != '\n')
					ut += temp2.charAt(temp2.length() - 1);
				Paragraph instruktioner = new Paragraph(ut);
				chapters[i].add(instruktioner);
				document.add(chapters[i]);
				i++;
			} catch (Exception e) {
				e.printStackTrace();
				document.close();
				System.exit(0);
			}

		}

		document.close();
		laddaUpp("C:\\Users\\Magnus\\Documents\\kokbok.pdf","kokbok");
	}

	public static void laddaUpp(String file, String target) {
		String server = "my.nada.kth.se";
		String userName = FetchPassword.fetchName(server);
		JSch jsch = new JSch();
		String knownHostsFilename = "C:\\cygwin\\home\\Magnus\\.ssh\\known_hosts";
		try {
			jsch.setKnownHosts(knownHostsFilename);
			Session session = jsch.getSession(userName, server, 22);
			String password = FetchPassword.fetchPassword(server);
			if (password.equals("")) {
				password = Password.lösenord(server, userName);
				FetchPassword.putPassword(server, password);
			}
			session.setPassword(password);
			session.connect();
			Channel channel = session.openChannel("sftp");
			channel.connect();
			ChannelSftp sftpChannel = (ChannelSftp) channel;
			InputStream src = new BufferedInputStream(new FileInputStream(
					file));
			sftpChannel.put(src, "public_html/"+target+".pdf");
			sftpChannel.exit();
			session.disconnect();
		} catch (JSchException e) {
			if (e.getMessage().equals("Auth fail")) {
				JOptionPane.showMessageDialog(new JFrame(),
						"Du har matat in fel lösenord", "Fel lösenord",
						JOptionPane.ERROR_MESSAGE);
				FetchPassword.putPassword(server, "");
				laddaUpp(file, target);
				return;
			}
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
}
