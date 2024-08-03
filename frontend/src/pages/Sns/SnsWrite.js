import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import ImgUpload from '../../components/Common/ImgUpload';
import Btn from '../../components/Common/Btn';
import Tab from '../../components/Sns/Tab';
import Content from '../../components/Common/Content';

import cameraIcon from '../../assets/icon/camera.png';

const SnsWrite = () => {
  const [imgs, setImgs] = useState([]);
  const [content, setContent] = useState('');

  const handleImageUpload = (event) => {
    const files = Array.from(event.target.files);
    if (files.length + imgs.length > 10) {
      alert('최대 10장까지 업로드할 수 있습니다.');
      return;
    }
    const newImgs = files.map(file => URL.createObjectURL(file));
    setImgs(prevImgs => [...prevImgs, ...newImgs]);
  };

  const handleDeleteImage = (index) => {
    setImgs(prevImgs => prevImgs.filter((_, i) => i !== index));
  };

  return (
    <div>
      <div>
        <Tab />
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
        <Content content={content} setContent={setContent} />
      </div>
      <div>
        <Btn content="작성하기" onClick={() => console.log('작성하기 버튼 클릭')} />
      </div>
    </div>
  );
};

export default SnsWrite;