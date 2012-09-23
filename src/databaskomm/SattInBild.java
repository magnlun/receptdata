package databaskomm;



import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import komponenter.Password;


import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import model.Connect;
import model.Lösenord;

public class SattInBild {
	public static void SattIn(BufferedImage bild, String recept,
			String protokoll) {
		int bredd = bild.getWidth(null);
		int höjd = bild.getHeight(null);
		double procent1 = 1;
		double procent2 = 1;
		if (bredd > 150) {
			procent1 = 150.0 / bredd;
		}
		if (höjd > 100) {
			procent2 = 100.0 / höjd;
		}
		double procent = Math.min(procent1, procent2);
		BufferedImage resizedImage = new BufferedImage((int) (bredd * procent),
				(int) (höjd * procent), bild.getType());
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(bild, 0, 0, (int) (bredd * procent),
				(int) (höjd * procent), null);
		g.dispose();
		laddaUppBild(resizedImage, protokoll, recept);
		Connection conn = Connect.getConnection("Databas");
		String query = "UPDATE \"Bakverk\" \n   SET \"BakNamn\"=\"BakNamn\", \"Betyg\"=\"Betyg\","
				+ " \"Instruktioner\"=\"Instruktioner\", \"Portioner\"=\"Portioner\","
				+ " \"Tidsåtgång (minuter)\"=\"Tidsåtgång (minuter)\", \n       \"Bild\"= '"
				+ recept
				+ "."
				+ protokoll
				+ "'\n WHERE \"BakNamn\" = '"
				+ recept + "';";

		try {
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	public static void laddaUppBild(BufferedImage image, String protokoll,
            String namn) {

    InputStream src = null;
    try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(image, protokoll, os);
            src = new ByteArrayInputStream(os.toByteArray());
    } catch (IOException e1) {
            e1.printStackTrace();
            System.exit(1);
    }
    String server = "my.nada.kth.se";
    String userName = Lösenord.fetchName(server);
    JSch jsch = new JSch();
    String knownHostsFilename = "C:\\cygwin\\home\\Magnus\\.ssh\\known_hosts";
    try {
            jsch.setKnownHosts(knownHostsFilename);
            Session session = jsch.getSession(userName, server, 22);
            String password = Lösenord.fetchPassword(server);
            if (password.equals("")) {
                    password = Password.lösenord(server, userName);
                    Lösenord.putPassword(server, password);
            }
            session.setPassword(password);
            session.connect();
            Channel channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp sftpChannel = (ChannelSftp) channel;
            String path = "public_html/Bilder/" + namn + "." + protokoll;
            sftpChannel.put(src, path);
            sftpChannel.exit();
            session.disconnect();
            return;
    } catch (JSchException e) {
            if (e.getMessage().equals("Auth fail")) {
                    JOptionPane.showMessageDialog(new JFrame(),
                                    "Du har matat in fel lösenord", "Fel lösenord",
                                    JOptionPane.ERROR_MESSAGE);
                    Lösenord.putPassword(server, "");
                    laddaUppBild(image, protokoll, namn);
                    return;
            }
            e.printStackTrace();
            System.exit(0);
    } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
    }

}
}
