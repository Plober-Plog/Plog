import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import API from '../../apis/api';

import ImgUpload from '../../components/Common/ImgUpload';
import Btn from '../../components/Common/Btn';
import Tab from '../../components/Sns/Tab';
import TextareaField from '../../components/Common/TextareaField';

import cameraIcon from '../../assets/icon/camera.png';

const SnsWrite = () => {
  const [imgs, setImgs] = useState([]);
  const [content, setContent] = useState('');
  const [selectedVisibility, setSelectedVisibility] = useState(1); // 공개 상태 관리
  const [tagTypeList, setTagTypeList] = useState([1, 2, 3]); // 태그 리스트 상태 추가

  // 이미지 업로드
  const handleImageUpload = (event) => {
    console.log(event.target.files);
    setImgs(Array.from(event.target.files)); // 파일 입력에서 파일 배열을 만들기
  };

  // 이미지 삭제
  const handleDeleteImage = (index) => {
    setImgs(prevImgs => prevImgs.filter((_, i) => i !== index));
  };

  // 작성버튼 클릭
  const handleSave = async () => {
    try {
      // FormData 생성
      const snsData = new FormData();
      const articleAddRequestDto = {
        content: content,
        images: [], // 이미지 파일 이름을 포함하는 배열
        tagTypeList: tagTypeList,
        visibility: selectedVisibility
      };

      imgs.forEach((img, index) => {
        snsData.append('images', img);  // 'images' key를 사용하여 각각의 파일을 추가
        articleAddRequestDto.images.push(`image${index}`); // 이미지 이름을 배열에 추가
      });

      snsData.append('articleAddRequestDto', JSON.stringify(articleAddRequestDto));
      
      console.log(Array.from(snsData.entries()));

      return;

      // 요청
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
