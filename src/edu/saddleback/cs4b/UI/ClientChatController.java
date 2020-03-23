package edu.saddleback.cs4b.UI;

import edu.saddleback.cs4b.Backend.Enums.MessageType;
import edu.saddleback.cs4b.Backend.Enums.ReceiveTypes;
import edu.saddleback.cs4b.Backend.Enums.SendTypes;
import edu.saddleback.cs4b.Backend.Messages.PicMessage;
import edu.saddleback.cs4b.Backend.Messages.RegMessage;
import edu.saddleback.cs4b.Backend.Messages.TextMessage;
import edu.saddleback.cs4b.Backend.Messages.UpdateMessage;
import edu.saddleback.cs4b.Backend.PubSub.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ClientChatController implements UISubject, ClientObserver
{
    private BlockingQueue<UIObserver> observers = new LinkedBlockingQueue<>();
    private Sendable data;
    private String username = "";
    private boolean registrationSent = false;
    private List<String> channels = new ArrayList<>();
    private String focusedChannel = "A";
    private String regErrorMsg = "";

    private Map<String, TextArea> channelToViewer = new Hashtable<>();

    @FXML
    private ListView<String> channelView;

    @FXML
    private ListView<String> userView;

    @FXML
    private TextField hostField;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField serverField;

    @FXML
    private TextField portField;

    @FXML
    private Label addChannel;

    @FXML
    private Label removeChannel;

    @FXML
    private Label sendMessage;

    @FXML
    private Label joinChat;

    @FXML
    private Label leaveChat;

    @FXML
    private Button addImageButton;

    @FXML
    private TextField channelName;

    @FXML
    private TextArea messageField;

    @FXML
    private TextArea chatArea;

    @FXML
    private StackPane stackPane;

    /**
     * WHEN THIS METHOD IS CALLED THIS WILL REGISTER THE USER TO A CHANNEL BY INDICATING THAT THEY HAVE JOINED THE CHANNEL
     **/
    @Override
    public void registerObserver(UIObserver o)
    {
        observers.add(o);
    }

    /**
     * WHEN THIS METHOD IS CALLED THIS WILL REMOVE THE OBSERVER FROM THE LIST OF OBSERVERS AND WILL NO LONGER GET NOTIFICATIONS
     **/
    @Override
    public void removeObserver(UIObserver o)
    {
        observers.remove(o);
    }

    /**
     * WHEN THIS METHOD IS CALLED THIS WILL NOTIFY OTHER OBSERVERS IN THAT CHANNEL TO ANY MESSAGES BEING SENT
     **/
    @Override
    public void notifyObservers()
    {
        Iterator<UIObserver> iterator = observers.iterator();
        while (iterator.hasNext()) {
            iterator.next().update(data);
        }
    }

    /**
     * WHEN THIS METHOD IS CALLED THIS WILL UPDATE THE MESSAGES BEING SENT
     **/
    @Override
    public void update(Receivable data)
    {
        if (data.getType().equals(ReceiveTypes.TEXT_AREA.getType()))
        {
            if(((UIDisplayData) data).getData() instanceof TextMessage)
            {
                TextMessage message = (TextMessage) ((UIDisplayData) data).getData();
                //chatArea.appendText(message + "\n");
                int i = stackPane.getChildren().indexOf(channelToViewer.get(message.getChannel()));
                if (i != -1) {
                    TextArea area = (TextArea) stackPane.getChildren().get(i);
                    area.appendText(message.getSender() + ": " + message.getMessage() + "\n");
                } else {
                    Platform.runLater(()-> {
                        stackPane.getChildren().add(new TextArea(message.getMessage()));
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Server unexpectedly shutdown, please exit and try again later");
                        alert.showAndWait();
                    });
                }
            }
            else if(((UIDisplayData) data).getData() instanceof PicMessage)
            {
                try {
                    PicMessage picMessage = (PicMessage) ((UIDisplayData) data).getData();
                    byte[] picBytes = picMessage.getImg();
                    String img = "Images/img.jpg";
                    File pictureMessage = new File(img);
                    FileOutputStream fout = new FileOutputStream(pictureMessage);
                    fout.write(picBytes);

                    Thread.sleep(700);

                    // using image view call new scene
                    Platform.runLater(()-> {
                        try {
                            Stage stage = new Stage();
                            Image image = new Image(new FileInputStream(pictureMessage), 200, 200 ,true, false);
                            ImageView imageView = new ImageView(image);
                            Scene scene = new Scene(new Pane(imageView), 200, 200);
                            stage.setScene(scene);
                            stage.setX(0);
                            stage.setY(0);
                            stage.show();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    });

                    // delete
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }

        }
        else if (data.getType().equals(ReceiveTypes.HOST.getType()))
        {
            // set the host value here which should be default at startup
            String hostName = (String)((UIDisplayData) data).getData();
            hostField.setText(hostName);
        }
    }

    /**
     * WHEN THIS METHOD IS CALLED THE ADD IMAGE BUTTON IS CLICKED AND ALLOWS THE USER TO SELECT A PICTURE TO DISPLAY
     **/
    @FXML
    public void onAddImageClicked(ActionEvent event)
    {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        FileChooser fileChooser = new FileChooser();

        // Set title for FileChooser
        fileChooser.setTitle("Select Pictures");

        // Set Initial Directory
        fileChooser.setInitialDirectory(new File("/Users"));

        // Add Extension Filters
        fileChooser.getExtensionFilters().addAll(//
                //new FileChooser.ExtensionFilter("All Files", "*.*"), //
                new FileChooser.ExtensionFilter("JPG", "*.jpg"), //
                new FileChooser.ExtensionFilter("PNG", "*.png"));


        addImageButton.setOnAction(event1 -> {
            File file = fileChooser.showOpenDialog(stage);
            if (file != null)
            {
                List<File> files = Arrays.asList(file);
                try {
                    printLog(chatArea, files);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * WHEN THIS METHOD IS CALLED THIS WILL INDICATE THAT THE PICTURE IS BEING ACCESSED FROM A PATH AND SAVED BY INPUTSTREAM
     **/
    private void printLog(TextArea textArea, List<File> files) throws IOException
    {
        if (files == null || files.isEmpty())
        {
            return;
        }
        for (File file : files)
        {
            String picSentMsg = username + "just uploaded a new image";
            data = new UIFields(SendTypes.MESSAGE, new TextMessage(username, focusedChannel, picSentMsg));
            notifyObservers();

            textArea.appendText(file.getAbsolutePath() + "\n");

            File picture = new File(file.getAbsolutePath());
            int len = (int) picture.length();
            byte[] bytes = new byte[len];
            InputStream fin = new BufferedInputStream(new FileInputStream(picture));
            fin.read(bytes);
            data = new UIFields(SendTypes.MESSAGE, new PicMessage(username, bytes, focusedChannel));
            notifyObservers();
        }
    }
    
    /**
     * WHEN THIS METHOD IS CALLED THIS WILL ADD A CHANNEL TO THE LIST VIEW OF CHANNELS
     **/
    @FXML
    void onChannelClicked(Event e) {
        ListView<String> ch = (ListView<String>)e.getSource();
        if (ch.getSelectionModel().getSelectedItem() != null) {
            focusedChannel = ch.getSelectionModel().getSelectedItem();
            int i = stackPane.getChildren().indexOf(channelToViewer.get(focusedChannel));
            TextArea focusedArea = (TextArea)stackPane.getChildren().get(i);
            stackPane.getChildren().remove(i);
            stackPane.getChildren().add(focusedArea);
        }
    }

    /**
     * WHEN THIS METHOD IS CALLED THIS WILL SEND THE MESSAGE WHEN THE LABEL IS CLICKED
     **/
    @FXML
    void onSendMessageClicked(Event e)
    {
        if (registrationSent)
        {
            String textInField = messageField.getText();
            String[] message = textInField.split("\n");
            data = new UIFields(SendTypes.MESSAGE, new TextMessage(username, focusedChannel, message[0]), focusedChannel);
            notifyObservers();
            messageField.clear();
        }
    }

    /**
     * WHEN THIS METHOD IS CALLED IF THE USER HITS ENTER INSTEAD OF SEND MEASSGE THE MESSAGE WILL STILL SEND
     **/
    @FXML
    void onEnterPressedInMessageField(KeyEvent e)
    {
        if (e.getCode() == KeyCode.ENTER)
        {
            onSendMessageClicked(e);
        }
    }

    /**
     * WHEN THIS METHOD IS CALLED THIS WILL UPDATE/SET THE USERNAME
     **/
    @FXML
    void setUserName(KeyEvent e)
    {
        if (e.getCode() == KeyCode.ENTER)
        {
            username = ((TextField) e.getSource()).getText();
        }

    }


    /**
     * WHEN THIS METHOD IS CALLED THE USER WILL JOIN THE CHAT
     **/
    @FXML
    void onClickJoinChat()
    {
        if (!registrationSent ) {
            if (!usernameField.getText().equals("") && validConfiguration() && channels.size() > 0 &&
                    validPort() && validServer()) {

                if (username.equals("")) {
                    username = usernameField.getText();
                }

                data = new UIFields(SendTypes.PORT_NUMBER, portField.getText(), "");
                notifyObservers();
                data = new UIFields(SendTypes.SERVER, serverField.getText(), "");
                notifyObservers();
                data = new Sendable() {
                    @Override
                    public String getType() {
                        return "connect";
                    }
                };
                notifyObservers();

                data = new UIFields(SendTypes.JOIN, new RegMessage(username, username, new ArrayList<>(channels)), focusedChannel);
                userView.getItems().add(username);
                notifyObservers();
                registrationSent = true;
                int i = stackPane.getChildren().indexOf(channelToViewer.get(focusedChannel));
                TextArea area = (TextArea)stackPane.getChildren().get(i);
                stackPane.getChildren().remove(i);
                stackPane.getChildren().add(area);
                portField.setDisable(true);
                usernameField.setDisable(true);
                serverField.setDisable(true);
            } else {
                stackPane.getChildren().remove(chatArea);
                stackPane.getChildren().add(chatArea);
                if (!regErrorMsg.equals("")) {
                    chatArea.appendText(regErrorMsg + "\n");
                } else {
                    chatArea.appendText("Could not register: Check you submitted a user name and channel\n");
                }
            }
        }
    }

    /**
     * WHEN THIS METHOD IS CALLED THIS WILL VALIDATE THE SERVER
     **/
    private boolean validServer() {
        String server = serverField.getText();
        if (!server.equals("localhost")) {
            regErrorMsg = "This server is invalid";
            return false;
        }
        return true;
    }

    /**
     * WHEN THIS METHOD IS CALLED THIS WILL VALIDATE THE PORT
     **/
    private boolean validPort() {
        int port;
        try {
            port = Integer.parseInt(portField.getText());
        } catch (NumberFormatException ne) {
            regErrorMsg = "Port must be a number";
            return false;
        }

        if (port != 8000) {
            regErrorMsg = "invalid port number";
            return false;
        }
        return true;
    }

    /**
     * WHEN THIS METHOD IS CALLED THIS WILL VALIDATE THE CONFIGURATION OF THE PROJECT
     **/
    private boolean validConfiguration()
    {
        return !serverField.getText().equals("") && !portField.getText().equals("");
    }

    /**
     * WHEN THIS METHOD IS CALLED WHEN THE USER WANTS TO EXIT THE CHAT PROGRAM THEY CAN CLICK ON LEAVE CHAT
     **/
    @FXML
    void onClickLeaveChat(Event e)
    {
        if (e.getSource() == leaveChat) {
            Stage s = (Stage)leaveChat.getScene().getWindow();
            s.close();
        }
    }

    /**
     * WHEN THIS METHOD IS CALLED THIS WILL ADD A CHANNEL TO THE LIST VIEW OF CHANNELS
     **/
    @FXML
    void onAddChannelClicked(Event e)
    {
        if (!channelName.getText().equals(""))
        {
            //channels.add(channelName.getText());
            if(channelView.getItems().isEmpty())
            {
               addChannel();
            }
            else
            {

                if (!channelView.getItems().contains(channelName.getText())) {
                    addChannel();
                }
            }
        }
        channelName.clear();
    }

    /**
     * WHEN THIS METHOD IS CALLED THIS WILL ADD A CHANNEL TO THE LIST OF CHANNELS
     **/
    private void addChannel()
    {
        focusedChannel = channelName.getText();
        TextArea txtArea = new TextArea();
        stackPane.getChildren().add(txtArea);
        channelToViewer.put(focusedChannel, txtArea);

        channels.add(channelName.getText());
        channelView.getItems().add(channelName.getText());
        data = new UIFields(SendTypes.CHANNEL, new UpdateMessage(username,
                MessageType.UPDATE.getType(), new ArrayList<>(channels)));
        notifyObservers();
    }

    /**
     * WHEN THIS METHOD IS CALLED THIS WILL REMOVE A CHANNEL FROM THE LIST VIEW OF CHANNELS DYNAMICALLY
     **/
    @FXML
    void onRemoveChannelClicked(Event e)
    {
        if (!channelName.getText().equals(""))
        {
            if(channelView.getItems().isEmpty())
            {
                channelName.clear();
            }
            else
            {
                if(channelView.getItems().contains(channelName.getText())) {
                    channels.remove(channelName.getText());
                    channelView.getItems().remove(channelName.getText());
                    data = new UIFields(SendTypes.CHANNEL, new UpdateMessage(username,
                            MessageType.UPDATE.getType(), new ArrayList<>(channels)), focusedChannel);
                    notifyObservers();

                    int i = stackPane.getChildren().indexOf(channelToViewer.get(channelName.getText()));
                    stackPane.getChildren().remove(i);
                    channelToViewer.remove(channelName.getText());
                }
            }
        }
        channelName.clear();
    }

    /**
     * WHEN THIS METHOD IS CALLED THE '+ ADD IMAGE' LABEL WILL CHANGE COLOR WHEN THE MOUSE IS HOVERING OVER IT
     */
    @FXML
    public void highlightAddImage()
    {
        addImageButton.setOnMouseEntered(mouseEvent -> addImageButton.setTextFill(Color.YELLOW));
    }

    /**
     * WHEN THIS METHOD IS CALLED THE '+ ADD IMAGE' LABEL WILL CHANGE BACK TO THE DEFAULT TEXT COLOR WHEN THE
     * MOUSE IS NO LONGER HOVERING OVER IT
     */
    @FXML
    public void resetAddImage()
    {
        addImageButton.setOnMouseExited(mouseEvent -> addImageButton.setTextFill(Color.WHITE));
    }

    /**
     * WHEN THIS METHOD IS CALLED THE 'SEND MESSAGE' LABEL WILL CHANGE COLOR WHEN THE MOUSE IS HOVERING OVER IT
     */
    @FXML
    public void highlightSendMessage()
    {
        sendMessage.setOnMouseEntered(mouseEvent -> sendMessage.setTextFill(Color.YELLOW));
    }

    /**
     * WHEN THIS METHOD IS CALLED THE 'SEND MESSAGE' LABEL WILL CHANGE BACK TO THE DEFAULT TEXT COLOR WHEN THE
     * MOUSE IS NO LONGER HOVERING OVER IT
     */
    @FXML
    public void resetSendMessage()
    {
        sendMessage.setOnMouseExited(mouseEvent -> sendMessage.setTextFill(Color.WHITE));
    }

    /**
     * WHEN THIS METHOD IS CALLED THE 'ADD CHANNEL' LABEL WILL CHANGE COLOR WHEN THE MOUSE IS HOVERING OVER IT
     */
    @FXML
    public void highlightAddChannel()
    {
        addChannel.setOnMouseEntered(mouseEvent -> addChannel.setTextFill(Color.YELLOW));
    }

    /**
     * WHEN THIS METHOD IS CALLED THE 'ADD CHANNEL' LABEL WILL CHANGE BACK TO THE DEFAULT TEXT COLOR WHEN THE
     * MOUSE IS NO LONGER HOVERING OVER IT
     */
    @FXML
    public void resetAddChannel()
    {
        addChannel.setOnMouseExited(mouseEvent -> addChannel.setTextFill(Color.WHITE));
    }

    /**
     * WHEN THIS METHOD IS CALLED THE 'REMOVE CHANNEL' LABEL WILL CHANGE COLOR WHEN THE MOUSE IS HOVERING OVER IT
     */
    @FXML
    public void highlightRemoveChannel()
    {
        removeChannel.setOnMouseEntered(mouseEvent -> removeChannel.setTextFill(Color.YELLOW));
    }

    /**
     * WHEN THIS METHOD IS CALLED THE 'REMOVE CHANNEL' LABEL WILL CHANGE BACK TO THE DEFAULT TEXT COLOR WHEN THE
     * MOUSE IS NO LONGER HOVERING OVER IT
     */
    @FXML
    public void resetRemoveChannel()
    {
        removeChannel.setOnMouseExited(mouseEvent -> removeChannel.setTextFill(Color.WHITE));
    }

    /**
     * WHEN THIS METHOD IS CALLED THE 'JOIN CHAT' LABEL WILL CHANGE COLOR WHEN THE MOUSE IS HOVERING OVER IT
     */
    @FXML
    public void highlightJoinChat()
    {
        joinChat.setOnMouseEntered(mouseEvent -> joinChat.setTextFill(Color.YELLOW));
    }

    /**
     * WHEN THIS METHOD IS CALLED THE 'JOIN CHAT' LABEL WILL CHANGE BACK TO THE DEFAULT TEXT COLOR WHEN THE
     * MOUSE IS NO LONGER HOVERING OVER IT
     */
    @FXML
    public void resetJoinChat()
    {
        joinChat.setOnMouseExited(mouseEvent -> joinChat.setTextFill(Color.WHITE));
    }

    /**
     * WHEN THIS METHOD IS CALLED THE 'LEAVE CHAT' LABEL WILL CHANGE COLOR WHEN THE MOUSE IS HOVERING OVER IT
     */
    @FXML
    public void highlightLeaveChat()
    {
        leaveChat.setOnMouseEntered(mouseEvent -> leaveChat.setTextFill(Color.YELLOW));
    }

    /**
     * WHEN THIS METHOD IS CALLED THE 'LEAVE CHAT' LABEL WILL CHANGE BACK TO THE DEFAULT TEXT COLOR WHEN THE
     * MOUSE IS NO LONGER HOVERING OVER IT
     */
    @FXML
    public void resetLeaveChat()
    {
        leaveChat.setOnMouseExited(mouseEvent -> leaveChat.setTextFill(Color.WHITE));
    }
}
