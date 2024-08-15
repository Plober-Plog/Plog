import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import './ChatListItem.css'; // Import the CSS file

const ChatListItem = ({ chatRoom, token }) => {
  const navigate = useNavigate();
  const API_REALTIME_URL = process.env.REACT_APP_REALTIME_BASE_URL;
  const [isSlid, setIsSlid] = useState(false);

  const handleEnterChatRoom = async (chatRoomId, chatRoomName) => {
    // 읽음 표시
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
    
    await chatRead();

    navigate(`/chat/${chatRoomId}`, {
      state: {
        chatRoomId,
        otherUserNickname: chatRoomName,
      },
    });
  };

  // 채팅방 삭제
  const handleChatDelete = async (e, chatRoomId) => {
    e.stopPropagation(); // 이벤트 버블링을 막아 클릭이 chat-list-item으로 전달되지 않도록 함
    try {
      const response = await axios.delete(`${API_REALTIME_URL}/chat/room`, {
        headers: {
          Authorization: token,
        },
        params: {
          chatRoomId: chatRoomId,  // params를 객체로 전달
        },
      });
      window.location.reload(); // 삭제 후 페이지 새로고침
    } catch (error) {
      console.error('채팅방 삭제 오류:', error);
    }
  };

  // 슬라이드 동작 처리
  const handleTouchStart = (e) => {
    setIsSlid(false); // 초기 상태로 설정
    e.target.startX = e.touches[0].clientX;
  };

  const handleTouchMove = (e) => {
    const deltaX = e.touches[0].clientX - e.target.startX;
    if (deltaX < -30) {
      setIsSlid(true);
    }
  };

  const handleTouchEnd = (e) => {
    // 필요한 경우 추가 처리
  };

  // 시간을 포맷팅하는 함수 (날짜는 제거하고 시간만)
  const formatTime = (timestamp) => {
    const date = new Date(timestamp);
    if (isNaN(date.getDate())) {
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
    <div 
      className={`chat-list-item-container ${isSlid ? 'slide-left' : ''}`} 
      onTouchStart={handleTouchStart}
      onTouchMove={handleTouchMove}
      onTouchEnd={handleTouchEnd}
    >
      <div className="chat-list-item" onClick={() => handleEnterChatRoom(chatRoom.chatRoom.chatRoomId, chatRoom.chatRoom.chatRoomName)}>
        <div className="chat-list-item-avatar">
          <img src={chatRoom.users[0].image.imageUrl} alt="Avatar" />
        </div>
        <div className="chat-list-item-content">
          <span className="chat-list-item-name">{chatRoom.users[0].nickname}</span>
          <span className="chat-list-item-message">{chatRoom.lastChat.message}</span>
        </div>
        <div className="chat-list-item-info">
          <span className="chat-list-item-date">{formatTime(chatRoom.lastChat.createdAt)}</span>
          {chatRoom.read === false && (
            <span className="chat-list-item-unread">N</span>
          )}
        </div>
      </div>
      <button 
        className="chat-delete-button"
        onClick={(e) => handleChatDelete(e, chatRoom.chatRoom.chatRoomId)}
      >
        채팅삭제
      </button>
    </div>
  );
};

export default ChatListItem;
