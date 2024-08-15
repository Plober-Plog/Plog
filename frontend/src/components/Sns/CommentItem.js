import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import API from '../../apis/api';
import useAuthStore from '../../stores/member';
import PropTypes from 'prop-types';
import ModalConfirm from '../Common/ModalConfirm'; // ModalConfirm 컴포넌트 임포트
import '../../pages/Sns/CommentDetail.css';
import arrow from '../../assets/icon/arrow.png'; // 화살표 아이콘 추가

const CommentItem = ({ comment, handleReply, onDelete }) => {  // onDelete prop 추가
  const navigate = useNavigate();
  const { isLogin, userData } = useAuthStore();
  const [showDeleteModal, setShowDeleteModal] = useState(false); // 모달 상태 추가
  const [deleted, setDeleted] = useState(comment.state !== 1); // 삭제된 상태인지 여부

  // 댓글 삭제 함수
  const handleCmtDelete = async (commentId) => {
    try {
      const response = await API.delete(`/user/sns/comment`, {
        params: { commentId }
      });
      setDeleted(true); // 댓글을 삭제된 상태로 변경
    } catch (error) {
      console.error('댓글 삭제 실패:', error.response);
    } finally {
      setShowDeleteModal(false); // 모달 닫기
    }
  };

  // 모달 열기
  const openDeleteModal = () => {
    setShowDeleteModal(true);
  };

  // 모달 닫기
  const closeDeleteModal = () => {
    setShowDeleteModal(false);
  };

  // 자식 댓글이면 화살표 추가
  const divStyle = comment.parentId !== comment.articleCommentId
    ? 'comment-child'
    : '';

  // 날짜 시간 수정
  const formattedDate = comment.createDate.slice(0, 10);

  return (
    <div className={`comment-item ${divStyle}`}>
      {deleted ? (
        <>
          {comment.parentId !== comment.articleCommentId && (
            <img src={arrow} alt="comment arrow icon" className="comment-arrow-icon" /> 
          )}
          <p className='mb-2'>삭제된 댓글입니다</p>
        </>
      ) : (
        <>
          {comment.parentId !== comment.articleCommentId && (
            <img src={arrow} alt="arrow icon" className="comment-arrow-icon" /> 
          )}
          <img 
            src={comment.profile} 
            className="comment-profile-img mt-4" 
            alt="profile" 
            onClick={() => navigate(`/profile/${comment.searchId}`)} 
          />
          <div className="comment-content">
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
              <h4>{comment.nickname}</h4>
              {isLogin && comment.searchId === userData.searchId && (
                <button 
                  className="comment-delete-button"
                  onClick={openDeleteModal} // 삭제 버튼 클릭 시 모달 열기
                >
                  댓글 삭제
                </button>
              )}
            </div>
            <p>{comment.content}</p>
            <p className="comment-date">
              {formattedDate}
              {comment.parentId === comment.articleCommentId && (
                <button 
                  className="comment-reply-button"
                  onClick={() => handleReply(comment.articleCommentId)}
                >
                  답글작성
                </button>
              )}
            </p>
          </div>
        </>
      )}

      {/* 삭제 확인 모달 */}
      <ModalConfirm
        open={showDeleteModal}
        onClose={closeDeleteModal}
        onConfirm={() => handleCmtDelete(comment.articleCommentId)} // 삭제 확인 시 실제 삭제 함수 호출
        title="댓글 삭제"
        content="정말 이 댓글을 삭제하시겠습니까?"
        confirmText="삭제하기"
      />
    </div>
  );
};

CommentItem.propTypes = {
  comment: PropTypes.shape({
    commentId: PropTypes.number.isRequired,
    articleCommentId: PropTypes.number.isRequired,
    profile: PropTypes.string,
    nickname: PropTypes.string,
    createDate: PropTypes.string,
    content: PropTypes.string,
    state: PropTypes.number.isRequired,
  }).isRequired,
  handleReply: PropTypes.func.isRequired,
  onDelete: PropTypes.func.isRequired,  // onDelete prop 추가
};

export default CommentItem;
