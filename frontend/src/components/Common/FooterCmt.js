import React, { useState, useEffect, useRef } from 'react';
import API from "../../apis/api";
import send from "../../assets/icon/footer/send.svg";
import './Footer.css';

const FooterCmt = ({ articleId, profile, setCommentList, selectedParentId, setSelectedParentId, isActive, setIsActive }) => {
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
      console.log('댓글 작성 성공:', response.data);
      setContent('');

      // 댓글 작성 후 댓글 목록 갱신
      const updatedComments = await API.get(`/user/sns/${articleId}/comment`);
      setCommentList(updatedComments.data);
      setIsActive(false);  // 댓글 작성 후 FooterCmt 비활성화
      setSelectedParentId(0);  // 작성 후 root 댓글 작성으로 초기화
    } catch (error) {
      console.error('댓글 작성 실패:', error.response || error);
    }
  };

  return (
    <div className={`footer-container ${isActive ? 'active' : ''}`}>
      <div>
        {profile && <img src={profile} alt="profile" />}
      </div>
      <form onSubmit={handleCmtWrite}>
        <div>
          <input
            type="text"
            placeholder="댓글을 입력해주세요"
            value={content}
            onChange={(e) => setContent(e.target.value)}
            ref={inputRef}
          />
        </div>
        <div>
          <button type="submit" className="footer-button">
            <img src={send} alt="send 아이콘" className='footer-icon' />
          </button>
        </div>
      </form>
    </div>
  );
};

export default FooterCmt;