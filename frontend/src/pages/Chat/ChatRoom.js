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
  const chatContainerRef = useRef(null);
  const token = getCookie('accessToken');
  const [messageContent, setMessageContent] = useState("");
  const [page, setPage] = useState(0);
  const [loading, setLoading] = useState(false);
  const [hasMore, setHasMore] = useState(true);
  const user = {
    nickname: userData.nickname,
    profile: userData.profile,
    searchId: userData.searchId,
  };
  console.log(user)

  // 채팅 내역 전체 가져오기
  const fetchChatHistory = async (page, isFirstLoad = false) => {
    if (loading || !hasMore) return;

    setLoading(true);

    // 이전 스크롤 위치와 컨텐츠 높이를 저장
    const prevScrollHeight = chatContainerRef.current?.scrollHeight;
    const prevScrollTop = chatContainerRef.current?.scrollTop;

    try {
      const response = await axios.get(`${API_REALTIME_URL}/chat/${chatRoomId}/history`, {
        headers: {
          Authorization: token,
        },
        params: {
          page: page,
        }
      });

      if (response.data.length === 0) {
        setHasMore(false);
      } else {
        setMessages(prevMessages => [...response.data.reverse(), ...prevMessages]);
        console.log('채팅 내역 가져오기 성공:', response.data);

        if (!isFirstLoad && prevScrollHeight && prevScrollTop !== undefined) {
          // 새 메시지를 불러온 후, 이전 스크롤 위치를 유지
          chatContainerRef.current.scrollTop = chatContainerRef.current.scrollHeight - prevScrollHeight + prevScrollTop;
        }

        if (isFirstLoad) {
          scrollToBottom();
        }
      }
    } catch (error) {
      console.error("채팅 내역을 가져오는 중 오류 발생:", error);
    } finally {
      setLoading(false);
    }
  };

  // 초기 채팅 내역 로드
  useEffect(() => {
    fetchChatHistory(page, true); // 첫 페이지의 채팅 내역을 로드할 때만 스크롤을 맨 밑으로 이동
  }, [chatRoomId, token]);

  // 스크롤을 맨 밑으로 이동시키는 함수
  const scrollToBottom = () => {
    if (chatContainerRef.current) {
      chatContainerRef.current.scrollTop = chatContainerRef.current.scrollHeight;
    }
  };

  // 새로운 메시지가 추가될 때만 스크롤을 맨 밑으로 이동
  useEffect(() => {
    if (messages.length && page === 0) {
      scrollToBottom();
    }
  }, [messages]);

  // 스크롤 맨 위 감지 및 페이지네이션 처리
  const handleScroll = () => {
    if (chatContainerRef.current.scrollTop === 0 && !loading && hasMore) {
      const nextPage = page + 1;
      setPage(nextPage);
      fetchChatHistory(nextPage);
    }
  };

  useEffect(() => {
    const container = chatContainerRef.current;
    container.addEventListener('scroll', handleScroll);
    return () => {
      container.removeEventListener('scroll', handleScroll);
    };
  }, [page, loading, hasMore]);

  // 메세지 보내기
  const sendMessage = (e) => {
    e.preventDefault(); // 폼 제출 방지

    // 메시지가 비어 있으면 아무 처리도 하지 않음
    if (!messageContent.trim()) return;

    console.log("보내는 메시지: " + messageContent);
    console.log('Send 메시지 전송');
    const sendData = {
      nickname: user.nickname,
      profile: user.profile,
      searchId: user.searchId,
      message: messageContent,
      chatType: "SEND",
      createdAt: new Date().toISOString()
    }
    console.log('SendData:', sendData);

    if (client && client.connected) {
      client.publish({
        destination: `/app/chat.sendMessage/${chatRoomId}`,
        headers: { 'Authorization': token },
        body: JSON.stringify(sendData)
      });
    }
    setMessageContent("");
    console.log('SendData 전송 완료');

    // 새로운 메시지를 보낸 후 스크롤을 맨 밑으로 이동
    setTimeout(() => {
      scrollToBottom();
    }, 100);
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
    <div ref={chatContainerRef}>
      <div className="chat-box">
        {messages.map((message, index) => (
          <div
            key={index}
            className={`chat-message-box ${message.nickname === user.nickname ? "sent" : ""}`}
          >
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
