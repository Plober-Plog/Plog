import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';
import React, { useEffect, useState, useRef, useLayoutEffect } from 'react';
import axios from 'axios';
import { useLocation, useParams } from 'react-router-dom';
import { getCookie } from '../../utils/cookieUtils';
import useAuthStore from '../../stores/member';
import send from '../../assets/icon/footer/send.png';
import './ChatRoom.css';

const API_REALTIME_URL = process.env.REACT_APP_REALTIME_BASE_URL;

const ChatRoom = () => {
  const location = useLocation();
  const { otherUserNickname } = location.state;
  const { chatRoomId } = useParams();
  const [client, setClient] = useState(null);
  const [messages, setMessages] = useState([]);
  const { userData } = useAuthStore();
  const chatContainerRef = useRef(null);
  const token = getCookie('accessToken');
  const [messageContent, setMessageContent] = useState("");
  const [page, setPage] = useState(0);
  const [isLastPage, setIsLastPage] = useState(false);
  const [scrollHeight, setScrollHeight] = useState(0);
  const user = {
    nickname: userData.nickname,
    profile: userData.profile,
    searchId: userData.searchId,
  };

  const fetchChatHistory = async (pageNum) => {
    if (isLastPage) return;

    try {
      const response = await axios.get(`${API_REALTIME_URL}/chat/${chatRoomId}/history`, {
        headers: {
          Authorization: token,
        },
        params: { page: pageNum }
      });

      if (response.data.length === 0) {
        setIsLastPage(true);
      } else {
        setMessages(prevMessages => [...response.data.reverse(), ...prevMessages]);
        setScrollHeight(chatContainerRef.current?.scrollHeight || 0);
      }
    } catch (error) {
      console.error("채팅 내역을 가져오는 중 오류 발생:", error);
    }
  };

  const scrollToBottom = () => {
    if (chatContainerRef.current) {
      chatContainerRef.current.scrollTop = chatContainerRef.current.scrollHeight;
    }
  };

  useEffect(() => {
    if (page > 0) {
      fetchChatHistory(page);
    }
  }, [page]);

  useLayoutEffect(() => {
    if (chatContainerRef.current && scrollHeight) {
      chatContainerRef.current.scrollTop = chatContainerRef.current.scrollHeight - scrollHeight;
    }
  }, [messages]);

  useEffect(() => {
    fetchChatHistory(page);
  }, [chatRoomId, token]);

  const handleScroll = () => {
    if (chatContainerRef.current.scrollTop === 0) {
      setPage(prevPage => prevPage + 1);
    }
  };

  const sendMessage = (e) => {
    e.preventDefault();

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

    setTimeout(scrollToBottom, 100);
  };

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
    scrollToBottom();
  };

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

      const chatRead = async () => {
        try {
          const response = await axios.post(`${API_REALTIME_URL}/chat/room/${chatRoomId}/read`, 
            { params: {chatRoomId} },
            {
              headers: {
                'Content-Type': 'application/json',
                Authorization: token,
              },
            }
          );
        } catch (error) {
          console.error('채팅 읽기 오류:', error);
        }
      };
      
      chatRead();
    }
  };

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
      },
      onWebSocketClose: () => {
        console.warn('WebSocket 연결이 종료되었습니다.');
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

  const formatTime = (timestamp) => {
    const date = new Date(timestamp);
    if (isNaN(date.getTime())) {
      return "Invalid Date";
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
      <div className='footerchat-container'>
        <form className="footerchat-form" onSubmit={sendMessage}>
            <input
              type="text"
              placeholder="메세지를 입력하세요"
              value={messageContent}
              onChange={(e) => setMessageContent(e.target.value)}
              onKeyUp={(e) => { 
                if (e.key === 'Enter') {
                    e.preventDefault();
                    sendMessage(e);
                }
              }}
              className="footerchat-input"
            />
            <button type="submit" className="footerchat-button">
              <img src={send} alt="send 아이콘" className="footerchat-icon" />
            </button>
        </form>
      </div>
    </div>
  );
};

export default ChatRoom;
