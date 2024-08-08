import React, { useState, useEffect } from 'react';
import API from "../../apis/api";
import send from "../../assets/icon/footer/send.svg";
import './Footer.css';

const FooterCmt = ({ articleId, profile, setCommentList, selectedParentId }) => {
  const [content, setContent] = useState('');

  useEffect(() => {
    setContent('');
  }, [selectedParentId]);

  const handleCmtWrite = async (e) => {
    e.preventDefault();

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
    } catch (error) {
      console.error('댓글 작성 실패:', error.response || error);
    }
  };

  return (
    <div className="footer-container">
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
