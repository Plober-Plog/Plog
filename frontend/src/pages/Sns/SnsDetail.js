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


const SnsDetail = () => {
  const navigate = useNavigate();
  const { articleId } = useParams()
  const [article, setArticle] = useState({});
  const [writerInfo, setWriterInfo] = useState({});

  const { userData } = useAuthStore();

  useEffect(() => {
    if (!articleId) {
      console.error('@@@Invalid access: No articleId found.');
      navigate('/sns');
      return;
    }

    const fetchArticle = async () => {
      try {
        const response = await API.get(`/user/sns/${articleId}`);
        console.log('@@@게시글 정보:', response.data);
        setArticle(response.data);
        const writerInfo = {
          profile: response.data.profile,
          recordDate: response.data.createdAt.slice(0, 10),
          nickname: response.data.nickname,
          searchId: response.data.searchId,
        }
        setWriterInfo(writerInfo);
        console.log('작성자 정보:', writerInfo);
      } catch (err) {
        console.error('@@@게시물 불러오기 실패 : ', err);
      }
    };
    fetchArticle();
  }, [articleId]);

  if (!article || !writerInfo.profile) {
    return <div>Loading...</div>;
  }

  const isAuthor = userData && userData.searchId === article.searchId;

  const handleSnsDlelete = async () => {
    try {
      const response = await API.delete(`/user/sns/${articleId}`);
      console.log('게시물 삭제 성공:', response.data);
      navigate('/sns');
    } catch (err) {
      console.error('게시물 삭제 실패 : ', err);
    }
  }

  const allTagsSelected = article.tagTypeList.map(tag => tag.tagTypeId);

  const handleProfileClick = () => {
    navigate(`/profile/${writerInfo.searchId}`)

    console.log(writerInfo)
  }

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
      
      <p>{article.content}</p>

      {isAuthor && (
        <>
          <div className="grid grid-cols-12 gap-3 mt-10 mb-5">
            <div className="col-span-6">
              <Btn content='수정하기' onClick={() => navigate('/sns/write', { state: { articleId } })} />
            </div>
            
            <div className="col-span-6">
              <Btn content='삭제하기' onClick={() => handleSnsDlelete()} />
            </div>
          </div>
        </>
      )}

      <div className="mb-5">
        <BtnList likeCnt={article.likeCnt} isLiked={article.isLiked} commentCnt={article.commentCnt} isBookmarked={article.isBookmarked} articleId={articleId} />
      </div>
      <Comment articleId={articleId}/>
    </div>
  );
}

export default SnsDetail;
