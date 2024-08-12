import axios from 'axios';
import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { getCookie } from '../../utils/cookieUtils';

const API_REALTIME_URL = "https://i11b308.p.ssafy.io/realtime";

const ChatRooms = () => {
  const [chatRooms, setChatRooms] = useState([]);
  const navigate = useNavigate();
  const token = getCookie('accessToken');  
  console.log(token);
  
  // 채팅방 불러오기 
  const fetchChatRooms = async () => {
    try {
      const response = await axios.get(`${API_REALTIME_URL}/chat/room`, {
        headers: {
          Authorization: token
        }
      });
      console.log(response);
      setChatRooms(response.data);
    } catch (error) {
      console.error('채팅방 목록을 불러오는 중 오류 발생:', error);
    }
  };

  useEffect(() => {
    
    fetchChatRooms();
  }, []);

  const handleEnterChatRoom = (chatRoomId, chatRoomName) => {
    navigate(`/chat/${chatRoomId}`, {
      state: {
        chatRoomId: chatRoomId,
        otherUserNickname: chatRoomName,
      }
    });
  };

  return (
    <div className="chat-room-container">
      <h1>채팅방 목록</h1>
      <ul>
        {chatRooms.map((room) => (
          <li key={room.chatRoomId} onClick={() => handleEnterChatRoom(room.chatRoomId, room.chatRoomName)}>
            {room.chatRoomName}
          </li>
        ))}
      </ul>
      {/* <button onClick={handleCreateChatRoom}>채팅방 개설</button> */}
    </div>
  );
};

export default ChatRooms;
