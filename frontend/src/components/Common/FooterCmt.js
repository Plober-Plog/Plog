import React, { useState, useEffect, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import API from "../../apis/api";
import send from "../../assets/icon/footer/send.png";
import './FooterCmt.css';

const FooterCmt = ({ articleId, profile, disable, placeholder, setCommentList, selectedParentId, setSelectedParentId, isActive, setIsActive }) => {
  const navigate = useNavigate();
  const [content, setContent] = useState('');
  const inputRef = useRef(null);

  useEffect(() => {
    if (isActive) {
      inputRef.current.focus();
    }
  }, [isActive]);

  useEffect(() => {
    setContent('');
  }, [selectedParentId]);

  const handleCmtWrite = async (e) => {
    e.preventDefault();

    if (!content.trim()) {
      alert("댓글 내용을 입력해주세요.");
      return;
    }

    const commentData = {
      articleId,
      content,
      parentId: selectedParentId,
    };

    try {
      const response = await API.post(`/user/sns/comment`, commentData);
      setContent('');

      // 댓글 작성 후 댓글 목록 갱신
      const updatedComments = await API.get(`/user/sns/${articleId}/comment`);
      setCommentList(updatedComments.data);
      setIsActive(false);  // 댓글 작성 후 FooterCmt 비활성화
      setSelectedParentId(0);  // 작성 후 root 댓글 작성으로 초기화
      navigate(`/sns/${articleId}/comment`, {state: { articleId }});
    } catch (error) {
      console.error('댓글 작성 실패:', error.response || error);
    }
  };

  return (
    <div className={`footercmt-container ${isActive ? 'footercmt-active' : ''}`}>
      <div>
        {profile && <img src={profile} alt="profile" className="footercmt-img" />}
      </div>
      <form onSubmit={handleCmtWrite} className="footercmt-form">
        <input
          type="text"
          placeholder={placeholder ? placeholder : "댓글을 작성하세요"}
          value={content}
          onChange={(e) => setContent(e.target.value)}
          ref={inputRef}
          className="footercmt-input"
          disabled={disable}
        />
        <button type="submit" className="footercmt-button">
          <img src={send} alt="send 아이콘" className="footercmt-icon" />
        </button>
      </form>
    </div>
  );
};

export default FooterCmt;
