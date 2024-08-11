import React, { useEffect, useState } from 'react';
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';

const API_REALTIME_URL = "https://i11b308.p.ssafy.io/realtime"; // realtime api 주소
// 1. 사용자를 식별할 JWT -> cookie에서 가져오기
const token = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaXNzIjoicGxvZy5jb20iLCJleHAiOjE3MjQwNDUzMTUsImlhdCI6MTcyMjgzNTcxNX0.BFVSXUtidgN3jrayov5V0wnDeU2QkItay86uQu2wf3o";

const ChatRoom = ({ chatRoomId }) => { // ChatRoomList에서 해당 채팅방을 클릭하면 chatRoomId를 가지고 페이지 이동
  const [client, setClient] = useState(null); // stomp client
  const [messages, setMessages] = useState([]);
  const [user, setUser] = useState({
    nickname: "nickname", // cookie에서 가져올 로그인한 회원 닉네임
    profile: "https://plogbucket.s3.ap-northeast-2.amazonaws.com/free-icon-sprout-267205.png", // cookie에서 가져올 로그인한 회원 프로필 사진
  });
  const [messageContent, setMessageContent] = useState("");

  useEffect(() => {
    const socket = new SockJS(`${API_REALTIME_URL}/chat/ws`); // sockjs 를 이용한 websocket 연결

    const stompClient = new Client({
      webSocketFactory: () => socket,
      connectHeaders: {
        Authorization: token
      },
      onConnect: (frame) => {
        console.log('WebSocket 연결 성공: ' + frame);
        stompClient.subscribe('/topic/chatroom-2', messageOutput => { // chatroom-${chatRoomId} 가 돼야 함! 
          showMessage(JSON.parse(messageOutput.body)); // subscriber들이 받을 publish 된 메시지 띄워주는 함수
        });

        console.log('Join 메시지 전송');
        stompClient.publish({
          destination: `/app/chat.addUser/2`, // /add.chat.addUser/${chatRoomId} 가 돼야 함
          headers: { 'Authorization': token }, // 토큰 가지고 넘기기
          body: JSON.stringify({
            nickname: user.nickname,
            profile: user.profile,
            message: `${user.nickname}님이 들어오셨습니다.`,
            chatType: "JOIN",
            createdAt: null
          })
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
    if (client && client.connected) {
      client.publish({
        destination: `/app/chat.sendMessage/2`, // 2 대신 chatRoomId
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
  };

  const leaveChat = (client) => {
    if (client && client.connected) {
      console.log('Leave 메시지 전송');
      client.publish({
        destination: `/app/chat.leaveUser/2`, // 2 대신 chatRoomId
        headers: { 'Authorization': token },
        body: JSON.stringify({
          nickname: user.nickname,
          profile: user.profile,
          message: `${user.nickname}님이 나갔습니다.`,
          chatType: "LEAVE",
          createdAt: null
        })
      });
    }
  };

  const showMessage = (message) => {
    console.log("수신한 메시지: " + JSON.stringify(message));
    setMessages(prevMessages => [...prevMessages, message]);
  };


  return (
    <div className="chat-container">
      <div className="offcanvas-title" style={{ height: "40px" }}>
        <span>테테테테스트</span>
      </div>
      <div className="chat-box" id="response">
        {messages.map((message, index) => (
          <div key={index}>
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
