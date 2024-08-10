import React, { useState, useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import API from '../../apis/api';

import ImgUpload from '../../components/Common/ImgUpload';
import Btn from '../../components/Common/Btn';
import Tab from '../../components/Sns/Tab';
import TextareaField from '../../components/Common/TextareaField';
import Tags from '../../components/Sns/Tags';

import cameraIcon from '../../assets/icon/camera.png';

const SnsWrite = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { articleId } = location.state;  // articleId가 없을 때 0으로 초기화
  const [imgs, setImgs] = useState([]);
  const [content, setContent] = useState('');
  const [selectedVisibility, setSelectedVisibility] = useState(1); // 공개 상태 관리
  const [tagTypeList, setTagTypeList] = useState([]); // 선택된 태그 리스트 상태
  const tags = [
    { tagTypeId: 1, tagName: '일지' },
    { tagTypeId: 2, tagName: '분석 레포트' },
    { tagTypeId: 3, tagName: '질문' },
    { tagTypeId: 4, tagName: '일기' },
    { tagTypeId: 5, tagName: '식물' },
    { tagTypeId: 6, tagName: '정보' },
  ];

  // 태그 선택
  const handleTagSelect = (id) => {
    setTagTypeList(prevTags =>
      prevTags.includes(id) ? prevTags.filter(tag => tag !== id) : [...prevTags, id]
    );
  };

  // 이미지 업로드
  const handleImageUpload = (event) => {
    console.log(event.target.files);
    setImgs(Array.from(event.target.files)); // 파일 입력에서 파일 배열을 만들기
  };

  // 이미지 삭제
  const handleDeleteImage = (index) => {
    setImgs(prevImgs => prevImgs.filter((_, i) => i !== index));
  };

  // 수정일 경우 게시물 가져오기
  useEffect(() => {
    if (articleId !== 0) {
      const fetchSns = async () => {
        try {
          const response = await API.get(`/user/sns/${articleId}`);
          console.log('게시물 :', response.data);

          // 게시물 정보 input에 로딩
          setContent(response.data.content);
          setSelectedVisibility(response.data.visibility);
          setTagTypeList(response.data.tagTypeList.map(tag => tag.id));
          setImgs(response.data.images);
        } catch (err) {
          console.error('게시물 불러오기 실패 : ', err);
        }
      };
      fetchSns();
    }
  }, [articleId]);

  // 게시물 작성 또는 수정
  const handleSave = async () => {
    // FormData 생성
    const snsData = new FormData();
    snsData.append('content', content);
    snsData.append('visibility', selectedVisibility);

    // 이미지 넣기
    imgs.forEach((img, index) => {
      snsData.append('images', img);  // 'images' key를 사용하여 각각의 파일을 추가
    });

    // 태그 넣기
    tagTypeList.forEach((tagType, index) => {
      snsData.append(`tagTypeList[${index}]`, tagType);  // 'tagTypeList' key를 사용하여 각각의 태그를 추가
    });

    // FormData 확인
    console.log(Array.from(snsData.entries()));

    try {
      let response;
      if (articleId === 0) {
        // 게시물 작성 요청
        response = await API.post('/user/sns', snsData, {
          headers: {
            'Content-Type': 'multipart/form-data',
          },
        });
        console.log('게시물 작성:', response.data);
      } else {
        // 게시물 수정 요청
        response = await API.patch(`/user/sns/${articleId}`, snsData, {
          headers: {
            'Content-Type': 'multipart/form-data',
          },
        });
        console.log('게시물 수정:', response.data);
      }

      // 성공적으로 처리된 응답 후 페이지 이동
      navigate(`/sns/${response.data.id}`);
    } catch (error) {
      console.error('저장 중 오류 발생:', error);
    }
  };

  return (
    <div>
      <div>
        <Tab
          selectedVisibility={selectedVisibility}
          setSelectedVisibility={setSelectedVisibility}
        />
      </div>
      <div>
        <Tags selectedTags={tagTypeList} onTagSelect={handleTagSelect} tags={tags} />
      </div>
      <div>
        <ImgUpload 
          cameraIcon={cameraIcon} 
          imgs={imgs} 
          handleImageUpload={handleImageUpload} 
          handleDeleteImage={handleDeleteImage} 
        />
      </div>
      <div>
        <TextareaField
          value={content}
          onChange={e => setContent(e.target.value)}
          placeholder='내용을 입력해주세요.'
        />
      </div>
      <div>
        <Btn content={articleId === 0 ? "작성하기" : "수정하기"} onClick={handleSave} />
      </div>
    </div>
  );
};

export default SnsWrite;
