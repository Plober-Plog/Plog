import React, { useRef } from 'react';
import PropTypes from 'prop-types';
import DiaryTodoIcon from '../../components/Diary/DiaryTodoIcon'; 
import ImgPreview from '../../components/Common/ImgPreview'; 
import Btn from '../../components/Common/Btn'; 

const ImgUpload = ({ cameraIcon, imgs, handleImageUpload, handleDeleteImage }) => {
  const fileInputRef = useRef(null);

  const handleClick = () => {
    fileInputRef.current.click();
  };

  return (
    <div>
      <Btn 
        content={<DiaryTodoIcon src={cameraIcon} alt="upload" />}
        onClick={handleClick}
        style={{ background: 'none', border: 'none', cursor: 'pointer' }}
      />
      <input
        type="file"
        ref={fileInputRef}
        style={{ display: 'none' }}
        multiple
        accept="image/*"
        capture="environment" // environment 주면 카메라로 직접 찍기 설정 및 갤러리 접근 가능
        onChange={(e) => {
          handleImageUpload(e);
          e.target.value = ''; // 사진을 삭제하고 나서도 같은 사진을 올릴수 있게 하기 위해 초기화
        }}
      />
      <div>
        {imgs.map((img, index) => (
          <ImgPreview key={index} src={img} onDelete={() => handleDeleteImage(index)} />
        ))}
      </div>
    </div>
  );
};

ImgUpload.propTypes = {
  uploadIcon: PropTypes.string.isRequired,
  imgs: PropTypes.arrayOf(PropTypes.string).isRequired,
  handleImageUpload: PropTypes.func.isRequired,
  handleDeleteImage: PropTypes.func.isRequired,
};

export default ImgUpload;