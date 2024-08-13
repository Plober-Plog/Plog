import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';
import React, { useEffect, useState, useRef, useLayoutEffect } from 'react';
import axios from 'axios';
import { useLocation, useParams } from 'react-router-dom';
import { getCookie } from '../../utils/cookieUtils';
import useAuthStore from '../../stores/member';
import send from '../../assets/icon/footer/send.png';
import './ChatRoom.css';

const API_REALTIME_URL = "https://i11b308.p.ssafy.io/realtime"; // realtime api 주소

const ChatRoom = () => {
  const location = useLocation();
  const { otherUserNickname } = location.state;
  const { chatRoomId } = useParams();
  const [client, setClient] = useState(null); // stomp client
  const [messages, setMessages] = useState([]);
  const { userData } = useAuthStore();
  const chatContainerRef = useRef(null);  // 채팅 컨테이너를 참조하기 위한 ref
  const token = getCookie('accessToken');
  const [messageContent, setMessageContent] = useState("");
  const [page, setPage] = useState(0);  // 현재 페이지 상태
  const [isLastPage, setIsLastPage] = useState(false);  // 마지막 페이지 여부 상태
  const [scrollHeight, setScrollHeight] = useState(0); // 이전 스크롤 높이 저장
  const user = {
    nickname: userData.nickname,
    profile: userData.profile,
    searchId: userData.searchId,
  };

  // 채팅 내역 페이지네이션으로 가져오기
  const fetchChatHistory = async (pageNum) => {
    if (isLastPage) return;  // 마지막 페이지면 더 이상 요청하지 않음

    try {
      const response = await axios.get(`${API_REALTIME_URL}/chat/${chatRoomId}/history`, {
        headers: {
          Authorization: token,
        },
        params: { page: pageNum }
      });

      if (response.data.length === 0) {
        setIsLastPage(true);  // 더 이상 데이터가 없으면 마지막 페이지로 설정
      } else {
        setMessages(prevMessages => [...response.data.reverse(), ...prevMessages]);  // 이전 메시지를 앞에 추가
        setScrollHeight(chatContainerRef.current?.scrollHeight || 0); // 새로운 메시지 추가 전 스크롤 높이 저장
      }
    } catch (error) {
      console.error("채팅 내역을 가져오는 중 오류 발생:", error);
    }
  };

  // 스크롤을 맨 아래로 이동하는 함수
  const scrollToBottom = () => {
    if (chatContainerRef.current) {
      chatContainerRef.current.scrollTop = chatContainerRef.current.scrollHeight;
    }
  };

  // 페이지 번호가 변경될 때마다 이전 메시지 로드
  useEffect(() => {
    if (page > 0) {
      fetchChatHistory(page);
    }
  }, [page]);

  // 메시지가 업데이트될 때마다 스크롤 위치를 복원
  useLayoutEffect(() => {
    if (chatContainerRef.current && scrollHeight) {
      chatContainerRef.current.scrollTop = chatContainerRef.current.scrollHeight - scrollHeight;
    }
  }, [messages]);

  // 초기 채팅 내역 로드
  useEffect(() => {
    fetchChatHistory(page);  // 첫 페이지 로드
  }, [chatRoomId, token]);

  // 스크롤 이벤트 핸들러
  const handleScroll = () => {
    if (chatContainerRef.current.scrollTop === 0) {
      setPage(prevPage => prevPage + 1);  // 페이지 번호 증가
    }
  };

  // 메세지 보내기
  const sendMessage = (e) => {
    e.preventDefault(); // 폼 제출 방지

    // 메시지가 비어 있으면 아무 처리도 하지 않음
    if (!messageContent.trim()) return;

    const sendData = {
      nickname: user.nickname,
      profile: user.profile,
      searchId: user.searchId,
      message: messageContent,
      chatType: "SEND",
      createdAt: new Date().toISOString()
    };

    if (client && client.connected) {
      client.publish({
        destination: `/app/chat.sendMessage/${chatRoomId}`,
        headers: { 'Authorization': token },
        body: JSON.stringify(sendData)
      });
    }
    setMessageContent("");
    console.log('발신한 메세지:', sendData);
    scrollToBottom();
  };

  // 수신한 메시지
  const showMessage = (message) => {
    const messageObj = {
      nickname: message.nickname,
      profile: message.image,
      searchId: message.searchId,
      message: message.message,
      createdAt: message.createdAt,
      chatType: message.chatType,
    };
    setMessages(prevMessages => [...prevMessages, messageObj]);
    console.log('수신한 메세지:', message)
    scrollToBottom();
  };

  // 채팅방 나가기
  const leaveChat = (client) => {
    if (client && client.connected) {
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
        stompClient.subscribe(`/topic/chatroom-${chatRoomId}`, messageOutput => {
          showMessage(JSON.parse(messageOutput.body));
        });

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
    <div>
      <div className="chat-box" ref={chatContainerRef} onScroll={handleScroll}>
        {messages
          .filter((message) => message.message && message.message.trim() !== '')
          .map((message, index) => (
            <div
              key={index}
              className={`chat-message-box ${message.searchId === user.searchId ? "sent" : "received"}`}
            >
              {message.searchId !== user.searchId && (
                <>
                  <img src={message.profile} className='chat-image' alt="Profile" />
                  <div className="chat-content">{message.message}</div>
                  <div className="chat-timestamp">{formatTime(message.createdAt)}</div>
                </>
              )}
              {message.searchId === user.searchId && (
                <>
                  <div className="chat-timestamp">{formatTime(message.createdAt)}</div>
                  <div className="chat-content">{message.message}</div>
                  <img src={message.profile} className='chat-image' alt="Profile" />
                </>
              )}
            </div>
          ))}
      </div>

      <div className='footercmt-container'>
        <form className="footercmt-form" onSubmit={sendMessage}>
            <input
              type="text"
              placeholder="메세지를 입력하세요"
              value={messageContent}
              onChange={(e) => setMessageContent(e.target.value)}
              onKeyUp={(e) => { if (e.key === 'Enter') sendMessage(e); }}
              className="footercmt-input"
            />
            <button type="submit" className="footercmt-button">
              <img src={send} alt="send 아이콘" className="footercmt-icon" />
            </button>
        </form>
      </div>
    </div>
  );
};

export default ChatRoom;
