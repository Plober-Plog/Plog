import React, { useEffect, useState, useRef } from 'react';
import { useParams } from 'react-router-dom';
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';
import axios from 'axios';
import { getCookie } from '../../utils/cookieUtils';
import { useLocation } from 'react-router-dom';
import useAuthStore from '../../stores/member';

const API_REALTIME_URL = "https://i11b308.p.ssafy.io/realtime"; // realtime api 주소

const ChatRoom = () => {
  const location = useLocation();
  const { otherUserNickname } = location.state;
  const { chatRoomId } = useParams();
  const [client, setClient] = useState(null); // stomp client
  const [messages, setMessages] = useState([]);
  const { userData } = useAuthStore();
  const [page, setPage] = useState(0);
  const [hasMore, setHasMore] = useState(true);
  const [loading, setLoading] = useState(false);

  const user = {
    nickname: userData.nickname,
    profile: userData.profile,
  };

  console.log(user)

  const [messageContent, setMessageContent] = useState("");
  const token = getCookie('accessToken');

  // 채팅 내역 가져오기 함수
  const fetchChatHistory = async () => {
    if (loading || !hasMore) return; // 현재 로딩 중이거나 더 이상 로드할 데이터가 없으면 종료

    setLoading(true);
    try {
      const response = await axios.get(`${API_REALTIME_URL}/chat/${chatRoomId}/history`, {
        headers: {
          Authorization: token,
        },
        params: {
          chatRoomId,
          page,
        }
      });

      if (response.data.length === 0) {
        setHasMore(false);
      } else {
        setMessages(prevChatList => page === 0 ? response.data : [...prevChatList, ...response.data]);
        setPage(prevPage => prevPage + 1);
      }
      console.log('채팅 내역 가져오기 성공:', response.data);
    } catch (error) {
      console.error("채팅 내역을 가져오는 중 오류 발생:", error);
    } finally {
      setLoading(false);
    }
  };

  // 웹 소켓 연결
  useEffect(() => {
    const socket = new SockJS(`${API_REALTIME_URL}/chat/ws`); // sockjs 를 이용한 websocket 연결

    const stompClient = new Client({
      webSocketFactory: () => socket,
      connectHeaders: {
        Authorization: token,  // connectHeaders에 토큰 추가
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


  // 페이지 네이션
  const handleScroll = () => {
    const chatBox = chatBoxRef.current;
    if (chatBox) {
      const scrollTop = chatBox.scrollTop;
      const scrollHeight = chatBox.scrollHeight;
      const clientHeight = chatBox.clientHeight;

      if (scrollTop <= 50 && hasMore && !loading) {
        fetchChatHistory();
      }
    }
  };

  useEffect(() => {
    const chatBox = chatBoxRef.current;
    chatBox.addEventListener('scroll', handleScroll);
    return () => chatBox.removeEventListener('scroll', handleScroll);
  }, [hasMore, loading]);

  useEffect(() => {
    fetchChatHistory(); // 초기 채팅 내역 로드
  }, [chatRoomId, token]);


  // 메시지 보내기
  const sendMessage = () => {
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
          createdAt: new Date().toISOString()  // 현재 시간
        })
      });
    }
    setMessageContent("");
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

  
  // 수신한 메시지
  const showMessage = (message) => {
    console.log("수신한 메시지: " + JSON.stringify(message));
    setMessages(prevMessages => [...prevMessages, message]);
  };


  // 시간을 포맷팅하는 함수 (날짜는 제거하고 시간만)
  const formatTime = (timestamp) => {
    const date = new Date(timestamp);
    if (isNaN(date.getTime())) {
      return "Invalid Date"; // 잘못된 날짜 처리
    }

    // 국제화 API를 이용하여 로컬 시간대를 고려한 포맷 적용
    return new Intl.DateTimeFormat('default', {
      hour: '2-digit',
      minute: '2-digit',
      hour12: false, // 24시간 형식
      timeZone: 'Asia/Seoul' // 특정 시간대로 설정 (여기서는 서울)
    }).format(date);
  };


  // 밑에서부터 스크롤
  const chatBoxRef = useRef(null);

  useEffect(() => {
    scrollToBottom();
  }, [messages]);

  const scrollToBottom = () => {
    if (chatBoxRef.current) {
      chatBoxRef.current.scrollTop = chatBoxRef.current.scrollHeight;
    }
  };


  return (
    <div className="chat-container">
      <div className="offcanvas-title" style={{ height: "40px" }}>
        <span>{otherUserNickname}</span> {/* 상대 프로필 닉네임 .....*/}
      </div>
      <div className="chat-box" id="response" ref={chatBoxRef} style={{ overflowY: 'auto', maxHeight: '80vh' }}>
        {messages.map((message, index) => (
          <div key={index}>
            <div className="message-content">
              <img src={message.profile} style={{ width: '40px', height: '40px' }} alt="Profile" />
              <div className="content">{message.message}</div>
              <div className="timestamp">{formatTime(message.createdAt)}</div> {/* 시간만 표시 */}
            </div>
          </div>
        ))}
      </div>
      <div className="input-container">
        <input className="form-control" type="text" id="message" placeholder="메시지를 입력하세요..."
          value={messageContent} onChange={(e) => setMessageContent(e.target.value)}
          onKeyUp={(e) => { if (e.key === 'Enter') sendMessage(); }} />
        <button onClick={sendMessage} className="btn btn-outline-secondary">전송</button>
      </div>
    </div>
  );
};

export default ChatRoom;
