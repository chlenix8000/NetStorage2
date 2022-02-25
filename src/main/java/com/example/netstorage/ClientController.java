package com.example.netstorage;

import io.netty.channel.*;
import io.netty.channel.socket.nio.NioSocketChannel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.nio.NioEventLoopGroup;

public class ClientController {
    Bootstrap bootstrap = new Bootstrap();

    @FXML
    Button logBtn;
    @FXML
    TextField log;
    @FXML
    PasswordField pass;
    @FXML
    Label lblLs;
    @FXML
    Label lblU;
    @FXML
    Label lblP;
    @FXML
    Label lblCu;
    @FXML
    Label lblCd;
    @FXML
    Button dwlBtn;
    @FXML
    Button delBtn;
    @FXML
    Button selBtn;
    @FXML
    Button uplBtn;
    @FXML
    public void initialize() {
        setAuthorized(false);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
                bootstrap.group(eventLoopGroup);
                bootstrap.channel(NioSocketChannel.class);
                System.out.println("Контрольная точка");
                bootstrap.handler(new ChannelInitializer<>() {
                    @Override
                    public void initChannel(Channel channel) throws Exception {
                        ChannelPipeline pipeline = channel.pipeline();
                        pipeline.addLast(new ClientHandler());
                        ChannelFuture channelFuture = bootstrap.connect("localhost", 45001).sync();
                        channelFuture.channel().closeFuture().sync();
                        Thread.currentThread().interrupt();
                                            }
                });
                eventLoopGroup.shutdownGracefully();
            }
        });
        System.out.println("Поток 2 запускается");
        t.start();
    }



    public void setAuthorized(boolean b) {
        if (b) {
            lblU.setVisible(false);
            lblP.setVisible(false);
            pass.setVisible(false);
            logBtn.setVisible(false);
            log.setVisible(false);
            lblCd.setVisible(true);
            lblCu.setVisible(true);
            dwlBtn.setVisible(true);
            delBtn.setVisible(true);
            selBtn.setVisible(true);
            uplBtn.setVisible(true);
        }
        else {
            lblLs.setText("Введите логин и пароль");
            lblU.setVisible(true);
            lblP.setVisible(true);
            pass.setVisible(true);
            logBtn.setVisible(true);
            log.setVisible(true);
            lblCd.setVisible(false);
            lblCu.setVisible(false);
            dwlBtn.setVisible(false);
            delBtn.setVisible(false);
            selBtn.setVisible(false);
            uplBtn.setVisible(false);
        }
    }

    public void btnLogin(ActionEvent actionEvent) {
            String loginMsg = new String("/auth " + log.getText() + " " + pass.getText());
            System.out.println(loginMsg);
    }
}