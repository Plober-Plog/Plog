import React, { useEffect, useState } from 'react';
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';
import './ChatRoom.css'; // CSS 스타일을 이 파일에 저장

const API_REALTIME_URL = "https://i11b308.p.ssafy.io/realtime";
const token = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaXNzIjoicGxvZy5jb20iLCJleHAiOjE3MjQwNDUzMTUsImlhdCI6MTcyMjgzNTcxNX0.BFVSXUtidgN3jrayov5V0wnDeU2QkItay86uQu2wf3o";

const ChatRoom = ({ chatRoomId }) => {
  const [client, setClient] = useState(null);
  const [messages, setMessages] = useState([]);
  const [user, setUser] = useState({
    nickname: "nickname",
    profile: "https://plogbucket.s3.ap-northeast-2.amazonaws.com/free-icon-sprout-267205.png",
  });
  const [messageContent, setMessageContent] = useState("");

  useEffect(() => {
    const socket = new SockJS(`${API_REALTIME_URL}/chat/ws`);

    const stompClient = new Client({
      webSocketFactory: () => socket,
      connectHeaders: {
        Authorization: token
      },
      onConnect: (frame) => {
        console.log('WebSocket 연결 성공: ' + frame);
        stompClient.subscribe('/topic/public', messageOutput => {
          showMessage(JSON.parse(messageOutput.body));
        });

        console.log('Join 메시지 전송');
        stompClient.publish({
          destination: `/app/chat.addUser/${chatRoomId}`,
          headers: { 'Authorization': token },
          body: JSON.stringify({
            nickname: user.nickname,
            profile: user.profile,
            message: `${user.nickname}님이 들어오셨습니다.`,
            chatType: "JOIN",
            createdAt: null
          })
        });
        showMessage({
          nickname: user.nickname,
          profile: user.profile,
          message: `${user.nickname}님이 들어오셨습니다.`,
          chatType: "JOIN",
          createdAt: null
        });
      },
      onStompError: (frame) => {
        console.error('WebSocket 연결 실패: ' + frame.headers['message']);
      }
    });

    stompClient.activate();
    setClient(stompClient);

    return () => {
      if (stompClient && stompClient.connected) {
        leaveChat(stompClient);
      }
      stompClient.deactivate();
    };
  }, [chatRoomId]);

  const sendMessage = () => {
    if (!messageContent.trim()) return;

    console.log("보내는 메시지: " + messageContent);
    console.log('Send 메시지 전송');
    console.log(`${chatRoomId}`)
    if (client && client.connected) {
      client.publish({
        destination: `/app/chat.sendMessage/1`,
        headers: { 'Authorization': token },
        body: JSON.stringify({
          nickname: user.nickname,
          profile: user.profile,
          message: messageContent,
          chatType: "SEND",
          createdAt: null
        })
      });
    }
    setMessageContent("");
    showMessage({
      nickname: user.nickname,
      profile: user.profile,
      message: messageContent,
      chatType: "SEND",
      createdAt: null
    });
  };

  const leaveChat = (client) => {
    if (client && client.connected) {
      console.log('Leave 메시지 전송');
      client.publish({
        destination: `/app/chat.leaveUser/1`,
        headers: { 'Authorization': token },
        body: JSON.stringify({
          nickname: user.nickname,
          profile: user.profile,
          message: `${user.nickname}님이 나갔습니다.`,
          chatType: "LEAVE",
          createdAt: null
        })
      });
      showMessage({
        nickname: user.nickname,
        profile: user.profile,
        message: `${user.nickname}님이 나갔습니다.`,
        chatType: "LEAVE",
        createdAt: null
      });
    }
  };

  const showMessage = (message) => {
    console.log("수신한 메시지: " + JSON.stringify(message));
    setMessages(prevMessages => [...prevMessages, message]);
  };

  const getMessageColor = (message) => {
    const colors = ["blue-message", "purple-message", "orange-message"];
    const index = message.nickname.length % colors.length;
    return colors[index];
  };

  return (
    <div className="chat-container">
      <div className="offcanvas-title" style={{ height: "40px" }}>
        <span>테테테테스트</span>
      </div>
      <div className="chat-box" id="response">
        {messages.map((message, index) => (
          <div key={index} className={message.chatType === "SEND" ? `send-message ${getMessageColor(message)}` : message.chatType === "JOIN" ? "join-message" : "leave-message"}>
            {message.chatType === "SEND" && <img src={message.profile} className="profile-img" alt="Profile Image" />}
            <div className="message-content">
              <div className="nickname">{message.nickname}</div>
              <div className="content">{message.message}</div>
            </div>
          </div>
        ))}
      </div>
      <div className="input-container">
        <input className="form-control" type="text" id="message" placeholder="메시지를 입력하세요..."
               value={messageContent} onChange={(e) => setMessageContent(e.target.value)}
               onKeyUp={(e) => { if (e.key === 'Enter') sendMessage(); }} />
        <button onClick={sendMessage} className="btn btn-outline-secondary">전송</button>
        <button onClick={() => leaveChat(client)} className="btn btn-outline-secondary">나가기</button> 
      </div>
    </div>
  );
};

export default ChatRoom;
