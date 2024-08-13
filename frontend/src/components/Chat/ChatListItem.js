import React from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import './ChatListItem.css'; // Import the CSS file

const ChatListItem = ({ chatRoom, token }) => {
  const navigate = useNavigate();
  const API_REALTIME_URL = "https://i11b308.p.ssafy.io/realtime";

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
        console.log('채팅 입장 성공:', response.data);
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

  return (
    <div className="chat-list-item" onClick={() => handleEnterChatRoom(chatRoom.chatRoom.chatRoomId, chatRoom.chatRoom.chatRoomName)}>
      <div className="chat-list-item-avatar">
        <img src={chatRoom.users[0].image.imageUrl} alt="Avatar" />
      </div>
      <div className="chat-list-item-content">
        <span className="chat-list-item-name">{chatRoom.chatRoom.chatRoomName}</span>
        <span className="chat-list-item-message">{chatRoom.lastChat.message}</span>
      </div>
      <div className="chat-list-item-info">
        <span className="chat-list-item-date">{chatRoom.lastChat.updatedAt}</span>
        {chatRoom.read === false && (
          <span className="chat-list-item-unread">N</span>
        )}
      </div>
    </div>
  );
};

export default ChatListItem;
