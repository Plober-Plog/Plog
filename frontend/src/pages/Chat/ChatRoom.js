import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';
import React, { useEffect, useState, useRef } from 'react';
import axios from 'axios';
import { useLocation, useParams } from 'react-router-dom';
import { getCookie } from '../../utils/cookieUtils';
import useAuthStore from '../../stores/member';
import send from "../../assets/icon/footer/send.png";
import './ChatRoom.css';

const API_REALTIME_URL = "https://i11b308.p.ssafy.io/realtime"; // realtime api 주소

const ChatRoom = () => {
  const location = useLocation();
  const { otherUserNickname } = location.state;
  const { chatRoomId } = useParams();
  const [client, setClient] = useState(null); // stomp client
  const [messages, setMessages] = useState([]);
  const { userData } = useAuthStore();
  const chatBoxRef = useRef(null);
  const token = getCookie('accessToken');
  const [messageContent, setMessageContent] = useState("");
  const user = {
    nickname: userData.nickname,
    profile: userData.profile,
  };

  // 채팅 내역 전체 가져오기
  const fetchChatHistory = async () => {
    try {
      const response = await axios.get(`${API_REALTIME_URL}/chat/${chatRoomId}/history`, {
        headers: {
          Authorization: token,
        }
      });

      setMessages(response.data);
      console.log('채팅 내역 가져오기 성공:', response.data);

    } catch (error) {
      console.error("채팅 내역을 가져오는 중 오류 발생:", error);
    }
  };

  // 초기 채팅 내역 로드 및 스크롤 하단 이동
  useEffect(() => {
    fetchChatHistory(); // 채팅 내역을 한 번에 로드
  }, [chatRoomId, token]);

  // 메세지 보내기
  const sendMessage = (e) => {
    e.preventDefault(); // 폼 제출 방지
    if (!messageContent.trim()) return;

    console.log("보내는 메시지: " + messageContent);
    console.log('Send 메시지 전송');

    if (client && client.connected) {
      client.publish({
        destination: `/app/chat.sendMessage/${chatRoomId}`,
        headers: { 'Authorization': token },
        body: JSON.stringify({
          nickname: user.nickname,
          profile: user.profile,
          message: messageContent,
          chatType: "SEND",
          createdAt: new Date().toISOString()
        })
      });
    }
    setMessageContent("");
  };

  // 수신한 메시지
  const showMessage = (message) => {
    console.log("수신한 메시지: " + JSON.stringify(message));
    setMessages(prevMessages => [...prevMessages, message]);
  };

  // 채팅방 나가기
  const leaveChat = (client) => {
    if (client && client.connected) {
      console.log('Leave 메시지 전송');
      client.publish({
        destination: `/app/chat.leaveUser/${chatRoomId}`,
        headers: { 'Authorization': token },
        body: JSON.stringify({
          nickname: user.nickname,
          profile: user.profile,
          chatType: "LEAVE",
          createdAt: null
        })
      });
    }
  };

  // 웹 소켓 연결 설정
  useEffect(() => {
    const socket = new SockJS(`${API_REALTIME_URL}/chat/ws`);
    const stompClient = new Client({
      webSocketFactory: () => socket,
      connectHeaders: {
        Authorization: token,
      },
      onConnect: (frame) => {
        console.log('WebSocket 연결 성공: ' + frame);
        stompClient.subscribe(`/topic/chatroom-${chatRoomId}`, messageOutput => {
          showMessage(JSON.parse(messageOutput.body));
        });

        console.log('Join 메시지 전송');
        stompClient.publish({
          destination: `/app/chat.addUser/${chatRoomId}`,
          headers: { 'Authorization': token },
          body: JSON.stringify({
            nickname: user.nickname,
            profile: user.profile,
            chatType: "JOIN",
            createdAt: new Date().toISOString(),
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
  }, [chatRoomId, token, user.nickname, user.profile]);

  // 스크롤을 맨 밑으로 이동시키는 함수
  const scrollToBottom = () => {
    if (chatBoxRef.current) {
      chatBoxRef.current.scrollTop = chatBoxRef.current.scrollHeight;
    }
  };

  // 메시지가 추가될 때마다 스크롤을 맨 밑으로 이동
  useEffect(() => {
    scrollToBottom();
  }, [messages]);

  // 시간을 포맷팅하는 함수 (날짜는 제거하고 시간만)
  const formatTime = (timestamp) => {
    const date = new Date(timestamp);
    if (isNaN(date.getTime())) {
      return "Invalid Date"; // 잘못된 날짜 처리
    }

    return new Intl.DateTimeFormat('default', {
      hour: '2-digit',
      minute: '2-digit',
      hour12: false,
      timeZone: 'Asia/Seoul'
    }).format(date);
  };

  return (
    <div className="chat-container">
      <div className="offcanvas-title" style={{ height: "40px" }}>
        <span>{otherUserNickname}</span>
      </div>
      <div className="chat-box" ref={chatBoxRef}>
        {messages.map((message, index) => (
          <div key={index} className="chat-message-box">
            <img src={message.profile} className='chat-image' alt="Profile" />
            <div className="chat-content">{message.message}</div>
            <div className="chat-timestamp">{formatTime(message.createdAt)}</div>
          </div>
        ))}
      </div>

      <div className='footercmt-container'>
        <form className="footercmt-form" onSubmit={sendMessage}>
          <div>
            <input
              type="text"
              placeholder="메세지를 입력하세요"
              value={messageContent}
              onChange={(e) => setMessageContent(e.target.value)}
              onKeyUp={(e) => { if (e.key === 'Enter') sendMessage(e); }}
              className="footercmt-input"
            />
          </div>
          <div>
            <button type="submit" className="footercmt-button">
              <img src={send} alt="send 아이콘" className="footercmt-icon" />
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default ChatRoom;
