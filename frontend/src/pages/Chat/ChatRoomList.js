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

  const handleCreateChatRoom = async () => {
    try {
      const response = await axios.post(`${API_REALTIME_URL}/chat/room`, {
        chatRoomType: 1,
        chatRoomName: "테스트"
      }, {
        headers: {
          Authorization: token
        }
      });
      console.log('새 채팅방 개설:', response.data);
      fetchChatRooms(); // 새로운 채팅방 생성 후 목록 갱신
    } catch (error) {
      console.error('채팅방 개설 중 오류 발생:', error);
    }
  };

  const handleEnterChatRoom = (chatRoomId) => {
    navigate(`/chat/${chatRoomId}`);
  };

  return (
    <div className="chat-room-container">
      <h1>채팅방 목록</h1>
      <ul>
        {chatRooms.map((room) => (
          <li key={room.chatRoomId} onClick={() => handleEnterChatRoom(room.chatRoomId)}>
            {room.chatRoomName}
          </li>
        ))}
      </ul>
      <button onClick={handleCreateChatRoom}>채팅방 개설</button>
    </div>
  );
};

export default ChatRooms;
