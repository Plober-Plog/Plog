import React, { useState, useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import PropTypes from 'prop-types';
import API from "../../apis/api";
import FooterCmt from '../Common/FooterCmt';
import Btn from '../Common/Btn';
import CommentItem from './CommentItem';
import useAuthStore from '../../stores/member';
import defaultProfile from '../../components/Account/defaultprofile.png';

const Comment = ({ articleId }) => {
  const navigate = useNavigate();
  const location = useLocation();
  const [commentList, setCommentList] = useState([]);
  const [cmtCnt, setCmtCnt] = useState(0);
  const [selectedParentId, setSelectedParentId] = useState(0);
  const [isFooterCmtActive, setIsFooterCmtActive] = useState(false);
  const [page, setPage] = useState(0);
  const [hasMoreComments, setHasMoreComments] = useState(true);

  const { isLogin, userData } = useAuthStore();

  // 댓글 불러오기
  useEffect(() => {
    const fetchComments = async () => {
      try {
        const response = await API.get(`/user/sns/${articleId}/comment`, {
          params: { page },
        });
        if (response.data.length === 0) {
          setHasMoreComments(false);
        } else {
          setCommentList(prevComments => [...prevComments, ...response.data]);
          setCmtCnt(prevCount => prevCount + response.data.length);
        }
        console.log('댓글 불러오기 성공:', response.data);
      } catch (err) {
        console.error('댓글 불러오기 실패 : ', err);
      }
    };

    if (hasMoreComments) {
      fetchComments();
    }
  }, [articleId, page]);

  // 스크롤 이벤트 핸들러
  useEffect(() => {
    const handleScroll = () => {
      if (window.innerHeight + document.documentElement.scrollTop >= document.documentElement.offsetHeight && hasMoreComments) {
        setPage(prevPage => prevPage + 1);
      }
    };

    window.addEventListener('scroll', handleScroll);
    return () => {
      window.removeEventListener('scroll', handleScroll);
    };
  }, [hasMoreComments]);

  const handleReply = (parentId) => {
    setSelectedParentId(parentId);
    setIsFooterCmtActive(true);
  };

  // 댓글 삭제 핸들러
  const handleDeleteComment = (commentId) => {
    setCommentList(prevComments => prevComments.filter(comment => comment.articleCommentId !== commentId));
    setCmtCnt(prevCount => prevCount - 1);
  };

  // /sns/${articleId} 페이지에서만 댓글을 3개로 제한
  const displayedComments = location.pathname === `/sns/${articleId}`
    ? commentList.slice(0, 3)
    : commentList;

  return (
    <div>
      <br />
      <h3>댓글</h3>
      <div>
        {displayedComments.map((comment) => (
          <CommentItem 
            key={comment.articleCommentId} 
            comment={comment} 
            handleReply={handleReply} 
            onDelete={handleDeleteComment}  
          />
        ))}
      </div>
      <div className='mb-10'>
        {location.pathname === `/sns/${articleId}` && commentList.length > 3 && (
          <Btn 
            content='댓글 자세히 보기'
            onClick={() => navigate(`/sns/${articleId}/comment`, { state: { articleId } })}
            cmtCnt={cmtCnt} 
          />
        )}
      </div>
      {isLogin ? (  // userData가 있을 때만 FooterCmt 렌더링
        <FooterCmt
          articleId={articleId}
          selectedParentId={selectedParentId}
          setSelectedParentId={setSelectedParentId}
          profile={userData.profile}
          setCommentList={setCommentList}
          isActive={isFooterCmtActive}
          setIsActive={setIsFooterCmtActive}
        />
      ) : (
        <FooterCmt
          articleId={articleId}
          profile={defaultProfile}
          selectedParentId={selectedParentId}
          setSelectedParentId={setSelectedParentId}
          setCommentList={setCommentList}
          setIsActive={setIsFooterCmtActive}
          disable={true}
          placeholder={"회원에게만 제공되는 서비스 입니다."}
        />
      )}
    </div>
  );
};

Comment.propTypes = {
  articleId: PropTypes.number.isRequired,
};

export default Comment;
