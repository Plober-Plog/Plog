import React from "react";
import axios from 'axios';
import { getCookie } from '../../utils/cookieUtils';
import { useNavigate } from 'react-router-dom';
import chatIcon from '../../assets/icon/chat-white.png';

const BtnChat = ({ userData }) => {
  const API_REALTIME_URL = process.env.REACT_APP_REALTIME_BASE_URL;
  const token = getCookie('accessToken');
  const navigate = useNavigate();

  const handleChat = async () => {
    try {
      const response = await axios.post(`${API_REALTIME_URL}/chat/room`, {
        targetSearchId: userData.searchId,
        chatRoomType: 1,
        chatRoomName: userData.nickname,
      }, {
        headers: {
          Authorization: token
        }
      });
      
      const chatRoomId = response.data.message;
      navigate(`/chat/${chatRoomId}`, {
        state: {
          chatRoomId: chatRoomId,
        }
      });
    } catch (error) {
      if (error.response && error.response.status === 409) {
        const chatRoomId = error.response.data.message;
        navigate(`/chat/${chatRoomId}`, {
          state: {
            chatRoomId: chatRoomId,
          }
        });
      } else {
        console.error('채팅방 개설 중 오류 발생:', error);
      }
    }
  };

  return (
    <button className="add-btn" onClick={handleChat}>
      <img src={chatIcon} alt="채팅 아이콘" />
    </button>
  ) 
}

export default BtnChat;
