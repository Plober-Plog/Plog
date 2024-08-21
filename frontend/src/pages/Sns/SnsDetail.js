<<<<<<< HEAD
import React from "react";
import { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";
import API from "../../apis/api";

const SnsDetail = () => {
  const location = useLocation();
  // const { articleId } = location.state;
  const articleId = 50; // 임시 번호

  const [article, setArticle] = useState(null);
  const [userInfo, setUserInfo] = useState(null);


  useEffect(() => {
    const fetchArticle = async () => {
      try {
        const response = await API.get(`/user/sns/${articleId}`);
        console.log('게시글 정보:', response.data);
        setArticle(response.data)

      } catch (err) {
        console.error('게시물 불러오기 실패 : ', err);
=======
import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import API from "../../apis/api";
import useAuthStore from "../../stores/member";

import WriterInfo from "../../components/Common/WriterInfo";
import Comment from '../../components/Sns/Comment';
import Tags from "../../components/Sns/Tags";
import ImgSlider from "../../components/Common/ImgSlider";
import BtnList from "../../components/Sns/BtnList";
import Btn from "../../components/Common/Btn";
import ModalConfirm from "../../components/Common/ModalConfirm"; // 모달 컴포넌트 임포트

const SnsDetail = () => {
  const navigate = useNavigate();
  const { articleId } = useParams();
  const [article, setArticle] = useState({});
  const [writerInfo, setWriterInfo] = useState({});
  const [showDeleteModal, setShowDeleteModal] = useState(false); // 삭제 모달 상태 추가

  const { userData } = useAuthStore();

  // 시간
  const formatDate = (dateString) => {
    const date = new Date(dateString);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0'); // 월은 0부터 시작하므로 +1
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    return `${year}.${month}.${day} ${hours}:${minutes}`;
  }

  useEffect(() => {
    if (!articleId) {
      console.error('@@@Invalid access: No articleId found.');
      navigate('/sns');
      return;
    }

    const fetchArticle = async () => {
      try {
        const response = await API.get(`/user/sns/${articleId}`);
        setArticle(response.data);
        const writerInfo = {
          profile: response.data.profile,
          recordDate: formatDate(response.data.createdAt),
          nickname: response.data.nickname,
          searchId: response.data.searchId,
        }
        setWriterInfo(writerInfo);
      } catch (err) {
        console.error('@@@게시물 불러오기 실패 : ', err);
>>>>>>> master
      }
    };
    fetchArticle();
  }, [articleId]);

<<<<<<< HEAD

  useEffect(() => {
    const fetchUser = async () => {
      try {
        const response = await API.get(`/user/profile/${article.searchId}`);
        console.log('유저 정보:', response.data);
        setUserInfo(response.data);
        
      } catch (err) {
        console.error('유저 정보 불러오기 실�� : ', err);
      }
    }

    fetchUser();

  }, [article])


  if (!article) {
    return <div>Loading...</div>; // 데이터를 가져오는 동안 로딩 상태를 표시
  }

  return (
    <div>
      <img src='' alt="게시글 이미지"/>
      <div>
        <p>{article.content}</p>
      </div>

=======
  if (!article || !writerInfo.profile) {
    return <div>Loading...</div>;
  }

  const isAuthor = userData && userData.searchId === article.searchId;

  // 게시물 삭제 함수
  const handleSnsDelete = async () => {
    try {
      const response = await API.delete(`/user/sns/${articleId}`);
      navigate('/sns');
    } catch (err) {
      console.error('게시물 삭제 실패 : ', err);
    }
  }

  const allTagsSelected = article.tagTypeList.map(tag => tag.tagTypeId);

  const handleProfileClick = () => {
    navigate(`/profile/${writerInfo.searchId}`)
  }


  // 모달 열기
  const openDeleteModal = () => {
    setShowDeleteModal(true);
  };

  // 모달 닫기
  const closeDeleteModal = () => {
    setShowDeleteModal(false);
  };

  return (
    <div className="mt-5">
      
      <div className="mb-4"
        onClick={handleProfileClick}
      >
        <WriterInfo data={writerInfo} type="user"
        />
      </div>
      
      <div className="mb-7">
        <ImgSlider imgs={article.images} />
      </div>
      
      <div className="mb-2" style={{ pointerEvents: 'none' }}>
        <Tags selectedTags={allTagsSelected} tags={article.tagTypeList} />
      </div>
      
      <p style={{ whiteSpace: 'pre-line' }}>{article.content}</p>

      {isAuthor && (
        <>
          <div className="grid grid-cols-12 gap-3 mt-10 mb-5">
            <div className="col-span-6">
              <Btn content='수정하기' onClick={() => navigate('/sns/write', { state: { articleId } })} />
            </div>
            
            <div className="col-span-6">
              <Btn content='삭제하기' onClick={openDeleteModal} /> {/* 모달을 열도록 수정 */}
            </div>
          </div>
        </>
      )}

      <div className="mb-5">
        <BtnList likeCnt={article.likeCnt} isLiked={article.isLiked} commentCnt={article.commentCnt} isBookmarked={article.isBookmarked} articleId={articleId} />
      </div>
      <Comment articleId={articleId}/>

      {/* 삭제 확인 모달 */}
      <ModalConfirm
        open={showDeleteModal}
        onClose={closeDeleteModal}
        onConfirm={handleSnsDelete} 
        title="게시물 삭제"
        content="정말 이 게시물을 삭제하시겠습니까?"
        confirmText="삭제하기"
      />
>>>>>>> master
    </div>
  );
}

<<<<<<< HEAD


export default SnsDetail;
=======
export default SnsDetail;
>>>>>>> master
