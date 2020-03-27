package edu.saddleback.cs4b.UI;

import edu.saddleback.cs4b.Backend.Enums.MessageType;
import edu.saddleback.cs4b.Backend.Enums.ReceiveTypes;
import edu.saddleback.cs4b.Backend.Enums.SendTypes;
import edu.saddleback.cs4b.Backend.History;
import edu.saddleback.cs4b.Backend.Messages.*;
import edu.saddleback.cs4b.Backend.PubSub.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

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

    private Map<String, TextArea> chatAreas = new Hashtable<>();
    private Map<String, StackPane> imageMapping = new Hashtable<>();

    @FXML
    private ListView<String> channelView;

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

    @FXML
    private StackPane picturePane;

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
                int i = stackPane.getChildren().indexOf(chatAreas.get(message.getChannel()));
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
            else if (((UIDisplayData) data).getData() instanceof RequestMessage)
            {
                RequestMessage requestMessage = (RequestMessage)((UIDisplayData) data).getData();
                downloadHistory(requestMessage);
            }
            else if(((UIDisplayData) data).getData() instanceof PicMessage)
            {
                try {
                    PicMessage picMessage = (PicMessage) ((UIDisplayData) data).getData();
                    //if (picMessage.getChannel().equals(focusedChannel)) {
                        byte[] picBytes = picMessage.getImg();
                        String img = "Images/img.jpg";
                        File pictureMessage = new File(img);
                        FileOutputStream fout = new FileOutputStream(pictureMessage);
                        fout.write(picBytes);

                        Thread.sleep(700);

                        // using image view call new scene
                        Platform.runLater(() -> {
                            try {
                                //Stage stage = new Stage();
                                Image image = new Image(new FileInputStream(pictureMessage), 200, 200, true, false);
                                //ImageView imageView = new ImageView(image);
                                int i = picturePane.getChildren().indexOf(imageMapping.get(picMessage.getChannel()));
                                StackPane pane = (StackPane) picturePane.getChildren().get(i);
                                ImageView imageView = (ImageView)pane.getChildren().get(0);
                                imageView.setImage(image);
//                                Scene scene = new Scene(new Pane(imageView), 200, 200);
//                                stage.setScene(scene);
//                                stage.setX(0);
//                                stage.setY(0);
//                                stage.show();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        });
                    //}
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
            hostField.setDisable(true);
        }
    }

    private void downloadHistory(RequestMessage requestMessage) {
        String channel = requestMessage.getChannel();
        List<String> history = ((History)requestMessage.getRequestable()).getTextLog();
        //byte[] img = ((History)requestMessage.getRequestable()).getFileData();

        // add image support later

        int i = stackPane.getChildren().indexOf(chatAreas.get(channel));
        TextArea area = (TextArea) stackPane.getChildren().get(i);
        for (String msg : history) {
            area.appendText(msg + "\n");
        }
        //area.appendText(message.getSender() + ": " + message.getMessage() + "\n");
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
            String picSentMsg = username + " just uploaded a new image";
            data = new UIFields(SendTypes.MESSAGE, new TextMessage(username, focusedChannel, picSentMsg));
            notifyObservers();
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
            int i = stackPane.getChildren().indexOf(chatAreas.get(focusedChannel));
            TextArea focusedArea = (TextArea)stackPane.getChildren().get(i);
            stackPane.getChildren().remove(i);
            stackPane.getChildren().add(focusedArea);

            int j = picturePane.getChildren().indexOf(imageMapping.get(focusedChannel));
            Pane pane = (StackPane)picturePane.getChildren().get(j);
            picturePane.getChildren().remove(j);
            picturePane.getChildren().add(pane);
        }
    }

    /**
     * WHEN THIS METHOD IS CALLED THIS WILL SEND THE MESSAGE WHEN THE LABEL IS CLICKED
     **/
    @FXML
    void onSendMessageClicked(Event e)
    {
        if (registrationSent && !messageField.getText().equals(""))
        {
            // todo bug: hold enter down, a bunch of newline feed cannot send
            // unless you enter a non-newline at which only a blank space appears
            String textInField = messageField.getText();
            String[] message = textInField.split("\n");
            if (message.length > 0) {
                data = new UIFields(SendTypes.MESSAGE, new TextMessage(username, focusedChannel, message[0]), focusedChannel);
                notifyObservers();
                messageField.clear();
            }
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
            if (!messageField.getText().equals("\n"))
            {
                onSendMessageClicked(e);
            }
            else
            {
                messageField.clear();
            }
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
                notifyObservers();
                registrationSent = true;
                int i = stackPane.getChildren().indexOf(chatAreas.get(focusedChannel));
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
            data = new UIFields(SendTypes.LEAVE, new DisconnectMessage(username, null));
            notifyObservers();
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
        generateNewTextArea();
        generateNewImageViewer();

        channels.add(channelName.getText());
        channelView.getItems().add(channelName.getText());
        // todo possible bug since registration message also sent with this
        data = new UIFields(SendTypes.CHANNEL, new UpdateMessage(username,
                MessageType.UPDATE.getType(), new ArrayList<>(channels)));
        notifyObservers();
    }

    private void generateNewTextArea()
    {
        focusedChannel = channelName.getText();
        data = new UIFields(SendTypes.HISTORY_REQUEST, new RequestMessage(username,
                RequestType.HISTORY, focusedChannel));
        notifyObservers();
        TextArea txtArea = new TextArea();
        stackPane.getChildren().add(txtArea);
        chatAreas.put(focusedChannel, txtArea);
    }

    private void generateNewImageViewer()
    {
        ImageView imgView = new ImageView();
        StackPane pane = new StackPane(imgView);
        pane.setStyle("-fx-background-color: white");
        picturePane.getChildren().add(pane);
        imageMapping.put(focusedChannel, pane);
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
                    setFocusedChannelAfterRemoval();
                }
            }
        }
        channelName.clear();
    }

    private void setFocusedChannelAfterRemoval()
    {
        int i = stackPane.getChildren().indexOf(chatAreas.get(channelName.getText()));
        stackPane.getChildren().remove(i);
        chatAreas.remove(channelName.getText());
        int size = stackPane.getChildren().size();
        TextArea topArea = (TextArea)stackPane.getChildren().get(size - 1);
        getAssociatedChannelWithArea(topArea);
        removeImageView();
    }

    private void removeImageView()
    {
        int i = picturePane.getChildren().indexOf(imageMapping.get(channelName.getText()));
        picturePane.getChildren().remove(i);
        imageMapping.remove(channelName.getText());

        int j = picturePane.getChildren().indexOf(imageMapping.get(focusedChannel));
        if (j != -1) {
            StackPane pane = (StackPane) picturePane.getChildren().get(j);
            picturePane.getChildren().remove(pane);
            picturePane.getChildren().add(pane);
        }
    }

    private void getAssociatedChannelWithArea(TextArea area)
    {
        for (Map.Entry<String, TextArea> e : chatAreas.entrySet()) {
            if (e.getValue().equals(area)) {
                focusedChannel = e.getKey();
            }
        }
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
