package main.java;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import main.java.gif.GifSequenceWriter.GifSequenceWriter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExternalFileOperations {

    public void saveAsGIF(ArrayList<SavedSlide> slideArrayList) {
        FileChooser fileChooser = new FileChooser();

        // Set extension filter for gif
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("GIF File", "*.gif");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show save file dialog
        File file = fileChooser.showSaveDialog(Main.primaryStage);

        if (file != null) {
            try {
                ArrayList<BufferedImage> imgList = toImages(slideArrayList);

                try (
                        final FileImageOutputStream outputStream = new FileImageOutputStream(file);
                        final GifSequenceWriter writer = new GifSequenceWriter(outputStream, imgList.get(0).getType(), 6000, false)
                ) {
                    for (BufferedImage img : imgList) {
                        writer.writeToSequence(img);
                    }
                }

            } catch (IOException e) {
                throwErrorMessage("Could not save as GIF", e);
            }
        }
    }

    public void saveAsHTML(ArrayList<SavedSlide> slideArrayList) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File file = directoryChooser.showDialog(Main.primaryStage);

        if (file != null) {
            try {
                ArrayList<BufferedImage> images = toImages(slideArrayList);
                ArrayList<String> imageNames = new ArrayList<String>(); //  To save the name of the generated series of images

                for (BufferedImage image : images) {
                    File savedImage = new File(file.getPath() + "/" + images.indexOf(image) + ".png");
                    ImageIO.write(image, "png", savedImage);
                    imageNames.add(savedImage.getName()); //  Store the name of the current image
                }

                generateHTML(imageNames, file.getPath());

            } catch (IOException e) {
                throwErrorMessage("Could not save as HTML", e);
            }
        }
    }

    public ArrayList<BufferedImage> toImages(ArrayList<SavedSlide> slideArrayList) throws IOException {
        ArrayList<BufferedImage> images = new ArrayList<>();
        BufferedImage endPane = ImageIO.read(this.getClass().getResource("/main/resources/images/end.png"));

        for(SavedSlide slide : slideArrayList) {
            BufferedImage pane = ImageIO.read(this.getClass().getResource("/main/resources/images/pane.png"));

            Graphics g = pane.getGraphics();
            ImageView left = slide.getCharacterLeft().getImage();
            ImageView right = slide.getCharacterRight().getImage();


            if(slide.getCharacterLeft().getScale() == -1) {
                g.drawImage(SwingFXUtils.fromFXImage(left.getImage(), null), 17+375, 342,-375, 375, null);
            }
            else {
                g.drawImage(SwingFXUtils.fromFXImage(left.getImage(), null), 17, 342,375, 375, null);
            }

            if(slide.getCharacterRight().getScale() == -1) {
                g.drawImage(SwingFXUtils.fromFXImage(right.getImage(), null), 402+375, 342, -375, 375, null);
            } else {
                g.drawImage(SwingFXUtils.fromFXImage(right.getImage(), null), 402, 342, 375, 375, null);
            }

            g.setColor(java.awt.Color.BLACK);

            //Narrative
            Font font = new Font("TimesRoman", Font.PLAIN, 25);
            g.setFont(font);
            FontMetrics metrics = g.getFontMetrics(font);
            int x = 5 + (790 - metrics.stringWidth(slide.getBelowNarrativeText())) / 2;
            g.drawString(slide.getAboveNarrativeText(), x, 50);
            x = 5 + (790 - metrics.stringWidth(slide.getBelowNarrativeText())) / 2;
            g.drawString(slide.getBelowNarrativeText(), x, 770);

            font = new Font("TimesRoman", Font.BOLD, 25);
            g.setFont(font);
            metrics = g.getFontMetrics(font);
            BufferedImage bubble;
            int length;
            int splitIndex;
            String firstLine;
            String secondLine;
            //Speech bubbles
            if(slide.getCharacterLeft().getBubble() != null) {
                bubble = ImageIO.read(this.getClass().getResource("/main/resources/images/"+slide.getCharacterLeft().getBubble().getName()+".png"));
                g.drawImage(bubble, 30, 135, null);
                if(metrics.stringWidth(slide.getCharacterLeft().getText()) <= 295) {//if text fits on 1 line
                    g.drawString(slide.getCharacterLeft().getText(), 200-(metrics.stringWidth(slide.getCharacterLeft().getText())/2), 250);
                }
                else {
                    length = slide.getCharacterLeft().getText().length();
                    splitIndex = slide.getCharacterLeft().getText().indexOf(" ", length/2);

                    if(splitIndex == -1) {
                        firstLine = slide.getCharacterLeft().getText().substring(0, length/2);
                        secondLine = slide.getCharacterLeft().getText().substring((length/2), length);
                    }
                    else {
                        firstLine = slide.getCharacterLeft().getText().substring(0, splitIndex);
                        secondLine = slide.getCharacterLeft().getText().substring(splitIndex+1, length);
                    }

                    g.drawString(firstLine, 200-(metrics.stringWidth(firstLine)/2), 250-metrics.getMaxDescent());
                    g.drawString(secondLine, 200-(metrics.stringWidth(secondLine)/2), 250+metrics.getMaxAscent());
                }
            }

            if(slide.getCharacterRight().getBubble() != null) {
                bubble = ImageIO.read(this.getClass().getResource("/main/resources/images/"+slide.getCharacterRight().getBubble().getName()+".png"));
                g.drawImage(bubble, 435, 135, null);
                if(metrics.stringWidth(slide.getCharacterRight().getText()) <= 295) {//if text fits on 1 line
                    g.drawString(slide.getCharacterRight().getText(), 603-(metrics.stringWidth(slide.getCharacterRight().getText())/2), 250);
                }
                else {
                    length = slide.getCharacterRight().getText().length();
                    splitIndex = slide.getCharacterRight().getText().indexOf(" ", length/2);

                    if(splitIndex == -1) {
                        firstLine = slide.getCharacterRight().getText().substring(0, length/2);
                        secondLine = slide.getCharacterRight().getText().substring((length/2), length);
                    }
                    else {
                        firstLine = slide.getCharacterRight().getText().substring(0, splitIndex);
                        secondLine = slide.getCharacterRight().getText().substring(splitIndex+1, length);
                    }

                    g.drawString(firstLine, 603-(metrics.stringWidth(firstLine)/2), 250-metrics.getMaxDescent());
                    g.drawString(secondLine, 603-(metrics.stringWidth(secondLine)/2), 250+metrics.getMaxAscent());
                }
            }

            images.add(pane);
        }
        images.add(endPane);

        return images;
    }

    private void generateHTML(ArrayList<String> imageNames, String exportPath) throws FileNotFoundException {
        int i = 0;
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<style>\n" +
                "table, th, td {\n" +
                "border: 10px solid white;\n" +
                "border-collapse: collapse;\n" +
                "}\n" +
                "</style>\n" +
                "</head>\n" +
                "<center>\n" +
                "<body style=\"background-color:white\">\n" +
                "<h2>Your Comic</h2>\n" +
                "<table>");

        for (String name : imageNames) {
            if (i % 2 == 0) {
                html.append("<tr>\n");
            }
            html.append("<td><center><img src=\"").append(name).append("\" width=\"500\" height=\"500\"></center></td>\n");

            if (i % 2 == 1) {
                html.append("<tr />\n");
            }

            i++;
        }

        if (i % 2 == 1) {
            html.append("<tr />\n");
        }

        html.append("</body>\n" +
                "</html>\n" +
                "</center>");

        File htmlFile = new File(exportPath + "/index.html");
        PrintStream printStream = new PrintStream(new FileOutputStream(htmlFile));
        printStream.println(html.toString());//将字符串写入文件
    }

    public void saveAsXML(ArrayList<SavedSlide> slideArrayList) {
        FileChooser fileChooser = new FileChooser();

        // Set extension filter for text files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show save file dialog
        File file = fileChooser.showSaveDialog(Main.primaryStage);

        if (file != null) {
            PrintWriter writer;
            try {
                writer = new PrintWriter(file);
                ArrayList<String> strings = toXML(slideArrayList);
                for (String string : strings) {
                    writer.println(string);
                }
                writer.close();
            } catch (FileNotFoundException e) {
                throwErrorMessage("Could not save as XML", e);
            }
        }
    }

    public ArrayList<String> toXML(ArrayList<SavedSlide> slideArrayList) {
        Color defaultSkin = new Color(255 / 255.0, 232 / 255.0, 216 / 255.0, 1);
        Color defaultHair = new Color(249 / 255.0, 255 / 255.0, 0 / 255.0, 1);

        Character character;

        ArrayList<String> strings = new ArrayList<>();
        strings.add("<?xml version = \"1.0\" encoding = \"UTF-8\"?>");
        strings.add("<comic>");
        strings.add("<panels>");

        for (SavedSlide slide : slideArrayList) {
            strings.add("<panel>");

            if (!slide.getAboveNarrativeText().isEmpty()) {
                strings.add("<above>" + slide.getAboveNarrativeText() + "</above>");
            }

            // Left Character
            strings.add("<left>");
            if (slide.getCharacterLeft() != null) {
                character = slide.getCharacterLeft();
                strings.add("<figure>");
                strings.add("<name>" + character.getName() + "</name>");
                strings.add("<appearance>" + character.getGender().toString() + "</appearance>");

                Color skin = character.getSkin();
                Color hair = character.getHairColor();
                String skinHex = "#" + (format(skin.getRed()) + format(skin.getGreen()) + format(skin.getBlue()) + format(skin.getOpacity())).toUpperCase();
                String hairHex = "#" + (format(hair.getRed()) + format(hair.getGreen()) + format(hair.getBlue()) + format(hair.getOpacity())).toUpperCase();

                // skin
                if (skin.equals(defaultSkin)) {
                    strings.add("<skin>default</skin>");
                } else {
                    strings.add("<skin>" + skinHex + "</skin>");
                }

                // hair
                if (hair.equals(defaultHair)) {
                    strings.add("<hair>default</hair>");
                } else {
                    strings.add("<hair>" + hairHex + "</hair>");
                }

                strings.add("<facing>" + character.getFacing().toString() + "</facing>");
                strings.add("</figure>");

                // bubble
                if (character.getBubble() != null) {
                    strings.add("<balloon status = \"" + character.getBubble().getName() + "\">");
                    strings.add("<content>" + character.getText() + "</content>");
                    strings.add("</balloon>");
                }
            }
            strings.add("</left>");

            // Right Character
            strings.add("<right>");
            if (slide.getCharacterRight() != null) {
                character = slide.getCharacterRight();
                strings.add("<figure>");
                strings.add("<name>" + character.getName() + "</name>");
                strings.add("<appearance>" + character.getGender().toString() + "</appearance>");

                Color skin = character.getSkin();
                Color hair = character.getHairColor();

                String skinHex = "#" + (format(skin.getRed()) + format(skin.getGreen()) + format(skin.getBlue()) + format(skin.getOpacity())).toUpperCase();
                String hairHex = "#" + (format(hair.getRed()) + format(hair.getGreen()) + format(hair.getBlue()) + format(hair.getOpacity())).toUpperCase();

                // skin
                if (skin.equals(defaultSkin)) {
                    strings.add("<skin>default</skin>");
                } else {
                    strings.add("<skin>" + skinHex + "</skin>");
                }

                // hair
                if (hair.equals(defaultHair)) {
                    strings.add("<hair>default</hair>");
                } else {
                    strings.add("<hair>" + hairHex + "</hair>");
                }

                strings.add("<facing>" + character.getFacing().toString() + "</facing>");
                strings.add("</figure>");

                // bubble
                if (character.getBubble() != null) {
                    strings.add("<balloon status = \"" + character.getBubble().getName() + "\">");
                    strings.add("<content>" + character.getText() + "</content>");
                    strings.add("</balloon>");
                }
            }
            strings.add("</right>");

            if (!slide.getBelowNarrativeText().isEmpty()) {
                strings.add("<below>" + slide.getBelowNarrativeText() + "</below>");
            }
            strings.add("</panel>");
        }

        strings.add("</panels>");
        strings.add("</comic>");

        return strings;
    }

    //helper method for converting color to hex
    private String format(double val) {
        String in = Integer.toHexString((int) Math.round(val * 255));
        return in.length() == 1 ? "0" + in : in;
    }

    public boolean validDocument(Document document) {
        if(!document.getDocumentElement().getNodeName().equalsIgnoreCase("comic")) {//comic tag missing
            return false;
        }

        Node panelsNode = document.getDocumentElement().getElementsByTagName("panels").item(0);
        if(panelsNode == null) {//panels tag missing
            return false;
        }
        return true;
    }

    public boolean validPanel(Element panel, ArrayList<Character> poseList) {
        Node leftNode = panel.getElementsByTagName("left").item(0);
        Node rightNode = panel.getElementsByTagName("right").item(0);

        Node leftFigureNode = ((Element) leftNode).getElementsByTagName("figure").item(0);
        Node rightFigureNode = ((Element) rightNode).getElementsByTagName("figure").item(0);

        Node leftFigureNameNode = ((Element) leftFigureNode).getElementsByTagName("name").item(0);
        Node rightFigureNameNode = ((Element) rightFigureNode).getElementsByTagName("name").item(0);


        if(leftNode == null || rightNode == null || leftFigureNode == null || rightFigureNode == null || leftFigureNameNode == null || rightFigureNameNode == null) {
            return false;
        }

        Element leftName = (Element) leftFigureNameNode;
        Element rightName = (Element) rightFigureNameNode;
        Character left = poseList.stream().filter(character -> leftName.getTextContent().equalsIgnoreCase(character.getName())).findAny().orElse(null);
        Character right = poseList.stream().filter(character -> rightName.getTextContent().equalsIgnoreCase(character.getName())).findAny().orElse(null);

        if(left == null || right == null) {
            return false;
        }

        return true;
    }

    public boolean validHex(String hex) {
        Pattern hexPattern = Pattern.compile("#([0-9a-fA-F]{3}|[0-9a-fA-F]{6}|[0-9a-fA-F]{8})");
        Matcher matcher = hexPattern.matcher(hex);
        return matcher.matches();
    }

    public SavedSlide loadPanel(Element panel, int id, ArrayList<Character> poseList, ArrayList<Bubble> bubbleList) {
        Element above = (Element) panel.getElementsByTagName("above").item(0);
        Element below = (Element) panel.getElementsByTagName("below").item(0);

        Element leftE = (Element) panel.getElementsByTagName("left").item(0);
        Element rightE = (Element) panel.getElementsByTagName("right").item(0);

        Element leftBalloon = (Element) leftE.getElementsByTagName("balloon").item(0);
        Element rightBalloon = (Element) rightE.getElementsByTagName("balloon").item(0);

        Element leftFigure = (Element) leftE.getElementsByTagName("figure").item(0);
        Element rightFigure = (Element) rightE.getElementsByTagName("figure").item(0);

        Element leftFigureName = (Element) leftFigure.getElementsByTagName("name").item(0);
        Element rightFigureName = (Element) rightFigure.getElementsByTagName("name").item(0);

        Element leftFigureAppearance =
                (Element) leftFigure.getElementsByTagName("appearance").item(0);
        Element rightFigureAppearance =
                (Element) rightFigure.getElementsByTagName("appearance").item(0);

        Element leftFigureSkin = (Element) leftFigure.getElementsByTagName("skin").item(0);
        Element rightFigureSkin = (Element) rightFigure.getElementsByTagName("skin").item(0);

        Element leftFigureHair = (Element) leftFigure.getElementsByTagName("hair").item(0);
        Element rightFigureHair = (Element) rightFigure.getElementsByTagName("hair").item(0);

        Element leftFigureFacing = (Element) leftFigure.getElementsByTagName("facing").item(0);
        Element rightFigureFacing =
                (Element) rightFigure.getElementsByTagName("facing").item(0);

        Character c;
        Bubble b;

        //left
        c = poseList.stream().filter(character -> leftFigureName.getTextContent().equalsIgnoreCase(character.getName())).findAny().orElse(null);
        Character left = new Character();
        left.setName(c.getName());
        left.setImage(new ImageView(c.getImage().getImage()));
        left.setText("");

        left.getImage().setFitHeight(150);
        left.getImage().setFitWidth(150);

        if(leftFigureAppearance != null && leftFigureAppearance.getTextContent().equalsIgnoreCase("MALE")) {
            left.setGender();
        }
        if(leftFigureSkin != null && validHex(leftFigureSkin.getTextContent())) {
            left.setSkin(Color.web(leftFigureSkin.getTextContent()));
        }
        if(leftFigureHair != null && validHex(leftFigureHair.getTextContent())) {
            left.setHairColor(Color.web(leftFigureHair.getTextContent()));
        }
        if(leftFigureFacing != null && leftFigureFacing.getTextContent().equalsIgnoreCase("RIGHT")) {
            left.setScale();
        }

        //left bubble
        if(leftBalloon != null && !leftBalloon.getAttribute("status").equals("")) {
            Element leftBalloonContent = (Element) leftBalloon.getElementsByTagName("content").item(0);
            b = bubbleList.stream().filter(bubble -> leftBalloon.getAttribute("status").equalsIgnoreCase(bubble.getName())).findAny().orElse(null);
            if(b != null && leftBalloonContent != null && !leftBalloonContent.getTextContent().equals("")) {
                left.setText(leftBalloonContent.getTextContent());
                Bubble leftBubble = new Bubble();
                leftBubble.setName(b.getName());
                leftBubble.setImage(new ImageView(b.getImage().getImage()));
                left.setBubble(leftBubble);
            }
        }


        //right
        c = poseList.stream().filter(character -> rightFigureName.getTextContent().equalsIgnoreCase(character.getName())).findAny().orElse(null);
        Character right = new Character();
        right.setName(c.getName());
        right.setImage(new ImageView(c.getImage().getImage()));
        right.setText("");

        right.getImage().setFitHeight(150);
        right.getImage().setFitWidth(150);

        if(rightFigureAppearance != null && rightFigureAppearance.getTextContent().equalsIgnoreCase("MALE")) {
            right.setGender();
        }
        if(rightFigureSkin != null && validHex(rightFigureSkin.getTextContent())) {
            right.setSkin(Color.web(rightFigureSkin.getTextContent()));
        }
        if(rightFigureHair != null && validHex(rightFigureHair.getTextContent())) {
            right.setHairColor(Color.web(rightFigureHair.getTextContent()));
        }
        if(rightFigureFacing != null && rightFigureFacing.getTextContent().equalsIgnoreCase("RIGHT")) {
            right.setScale();
        }

        if(rightBalloon != null && !rightBalloon.getAttribute("status").equals("")) {
            Element rightBalloonContent = (Element) rightBalloon.getElementsByTagName("content").item(0);
            b = bubbleList.stream().filter(bubble -> rightBalloon.getAttribute("status").equalsIgnoreCase(bubble.getName())).findAny().orElse(null);
            if(b != null && rightBalloonContent != null && !rightBalloonContent.getTextContent().equals("")) {
                right.setText(rightBalloonContent.getTextContent());
                Bubble rightBubble = new Bubble();
                rightBubble.setName(b.getName());
                rightBubble.setImage(new ImageView(b.getImage().getImage()));
                right.setBubble(rightBubble);
            }
        }

        String aboveNarrative = " ";
        String belowNarrative = " ";
        //panel text
        if(above != null) {
            aboveNarrative = above.getTextContent();
        }
        if(below != null) {
            belowNarrative = below.getTextContent();
        }

        SavedSlide slide = new SavedSlide(id, left, right, aboveNarrative, belowNarrative);
        return slide;
    }

    public ArrayList<SavedSlide> loadXML(ArrayList<Character> poseList, ArrayList<Bubble> bubbleList) {
        FileChooser fileChooser = new FileChooser();

        // Set extension filter for text files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show save file dialog
        File file = fileChooser.showOpenDialog(Main.primaryStage);

        if (file != null) {
            try {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(file);
                doc.getDocumentElement().normalize();

                if(validDocument(doc)) {
                    Node panelsNode = doc.getDocumentElement().getElementsByTagName("panels").item(0);
                    Element panelsElement = (Element) panelsNode;
                    NodeList panels = panelsElement.getElementsByTagName("panel");

                    ArrayList<Integer> unloadedPanels = new ArrayList<>();
                    ArrayList<SavedSlide> loadedPanels = new ArrayList<>();
                    int id = 0;
                    // get each panel
                    for (int i = 0; i < panels.getLength(); i++) {
                        Element panel = (Element) panels.item(i);
                        if(validPanel(panel, poseList)) {
                            loadedPanels.add(loadPanel(panel, id, poseList, bubbleList));
                            id++;
                        } else {
                            unloadedPanels.add(i+1);
                        }
                    }

                    //displays alert if some panels were not valid
                    if(!unloadedPanels.isEmpty()) {
                        StringBuilder message = new StringBuilder("The following panels could not be loaded due to syntax problems: ");
                        if(unloadedPanels.size()>1) {
                            for(Integer i : unloadedPanels.subList(0, unloadedPanels.size()-1)) {
                                message.append(i).append(", ");
                            }
                        }
                        message.append(unloadedPanels.get(unloadedPanels.size()-1)).append(".");
                        String title = "Problem loading "+file.getName();
                        throwMessage(title, message.toString(), new Alert(Alert.AlertType.WARNING));
                    }
                    return loadedPanels;
                } else {
                    //alert the user
                    throwMessage("Could not load "+file.getName(), "Syntax of file is invalid", new Alert(Alert.AlertType.WARNING));
                }
            } catch (Exception e) {
                throwErrorMessage("File could not be loaded", e);
            }
        }
        return null;
    }

    public void throwErrorMessage(String error, Exception f) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(error);
        alert.setContentText(f.getMessage());
        alert.showAndWait();
    }

    public void throwMessage(String title, String content, Alert alert) {
        alert.setTitle("Warning");
        alert.setHeaderText(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
