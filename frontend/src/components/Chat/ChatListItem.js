import React from 'react';
import { useNavigate } from 'react-router-dom';
import './ChatListItem.css'; // Import the CSS file

const ChatListItem = ({ chatRoom }) => {
  const navigate = useNavigate();

  const handleEnterChatRoom = (chatRoomId, chatRoomName) => {
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
        {chatRoom.lastChat.read === false && (
          <span className="chat-list-item-unread">New</span>
        )}
      </div>
    </div>
  );
};

export default ChatListItem;
