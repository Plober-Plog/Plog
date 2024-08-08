import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import API from '../../apis/api';

import ImgUpload from '../../components/Common/ImgUpload';
import Btn from '../../components/Common/Btn';
import Tab from '../../components/Sns/Tab';
import TextareaField from '../../components/Common/TextareaField';
import Tags from '../../components/Sns/Tags';

import cameraIcon from '../../assets/icon/camera.png';

const SnsWrite = () => {
  const [imgs, setImgs] = useState([]);
  const [content, setContent] = useState('');
  const [selectedVisibility, setSelectedVisibility] = useState(1); // 공개 상태 관리
  const [tagTypeList, setTagTypeList] = useState([]); // 선택된 태그 리스트 상태
  const tags = [
    { id: 1, label: '일지' },
    { id: 2, label: '분석 레포트' },
    { id: 3, label: '질문' },
    { id: 4, label: '일기' },
    { id: 5, label: '식물' },
    { id: 6, label: '정보' },
    { id: 7, label: '룸꾸미기' }
  ];

  
  // 태그 선택
  const handleTagSelect = (id) => {
    setTagTypeList(prevTags =>
      prevTags.includes(id) ? prevTags.filter(tag => tag !== id) : [...prevTags, id]
    );
  };
  // 태그 변경 확인
  useEffect(() => {
    console.log(tagTypeList);
  }, [tagTypeList]);


  // 이미지 업로드
  const handleImageUpload = (event) => {
    console.log(event.target.files);
    setImgs(Array.from(event.target.files)); // 파일 입력에서 파일 배열을 만들기
  };


  // 이미지 삭제
  const handleDeleteImage = (index) => {
    setImgs(prevImgs => prevImgs.filter((_, i) => i !== index));
  };


  // 게시물 작성
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
      // 게시물 작성 요청
      const response = await API.post('/user/sns', snsData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      });
  
      // 응답 처리
      console.log(response.data);
    } catch (error) {
      console.error(error);
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
        <Btn content="작성하기" onClick={handleSave} />
      </div>
    </div>
  );
};

export default SnsWrite;
