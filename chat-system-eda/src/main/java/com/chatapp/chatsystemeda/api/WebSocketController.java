//package com.chatapp.chatsystemeda.api;
//
//import com.chatapp.chatsystemeda.commandbus.CommandBus;
//import com.chatapp.chatsystemeda.commands.JoinRoomCommand;
//import com.chatapp.chatsystemeda.commands.LeaveRoomCommand;
//import com.chatapp.chatsystemeda.commands.SendMessageCommand;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.stereotype.Controller;
//
//@Controller
//public class WebSocketController {
//
//    private final CommandBus commandBus;
//
//    public WebSocketController(CommandBus commandBus) {
//        this.commandBus = commandBus;
//    }
//
//    @MessageMapping("/send")
//    public void sendMessage(SendMessageCommand cmd) {
//        commandBus.dispatch(cmd);
//    }
//
//    @MessageMapping("/join")
//    public void joinRoom(JoinRoomCommand cmd) {
//        commandBus.dispatch(cmd);
//    }
//
//    @MessageMapping("/leave")
//    public void leaveRoom(LeaveRoomCommand cmd) {
//        commandBus.dispatch(cmd);
//    }
//}
