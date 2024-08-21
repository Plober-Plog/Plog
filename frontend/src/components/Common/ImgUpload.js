import React, { useRef } from 'react';
import PropTypes from 'prop-types';
<<<<<<< HEAD
import DiaryTodoIcon from '../../components/Diary/DiaryTodoIcon'; 
import ImgPreview from '../../components/Common/ImgPreview'; 
import Btn from '../../components/Common/Btn'; 

const ImgUpload = ({ cameraIcon, imgs, handleImageUpload, handleDeleteImage, isDisabled }) => {
  const fileInputRef = useRef(null);

  const handleClick = () => {
    if (!isDisabled) {
      fileInputRef.current.click();
    }
  };

  return (
    <div>
      <Btn 
        content={<DiaryTodoIcon src={cameraIcon} alt="upload" />}
        onClick={handleClick}
        style={{ 
          background: 'none', 
          border: 'none', 
          cursor: 'pointer',
          filter: isDisabled ? 'grayscale(100%)' : 'none' // 비활성화 시 회색으로 변경
        }}
        disabled={isDisabled}
      />
=======
import ImgPreview from './ImgPreview'; 
import DiaryTodoIcon from '../Diary/DiaryTodoIcon';

const ImgUpload = ({ cameraIcon, imgs, handleImageUpload, handleDeleteImage }) => {
  const fileInputRef = useRef(null);

  const handleClick = () => {
    fileInputRef.current.click();
  };

  return (
    <div className="img-upload-container">
      <button 
        onClick={handleClick}
        className="img-upload-button"
      >
        <DiaryTodoIcon src={cameraIcon} alt="upload" />
      </button>
>>>>>>> master
      <input
        type="file"
        ref={fileInputRef}
        style={{ display: 'none' }}
        multiple
        accept="image/*"
<<<<<<< HEAD
        capture="environment"
        onChange={(e) => {
          if (!isDisabled) {
            handleImageUpload(e);
            e.target.value = '';
          }
        }}
        disabled={isDisabled}
      />
      <div>
        {imgs.map((img, index) => (
          <ImgPreview 
            key={index} 
            src={img.src} 
            onDelete={() => handleDeleteImage(index)} 
            isDisabled={isDisabled} // 비활성화 상태 전달
=======
        onChange={(e) => {
          handleImageUpload(e);
          e.target.value = ''; // Reset the input so that the same file can be uploaded again if needed
        }}
      />
      <div className="img-previews">
        {imgs.map((img, index) => (
          <ImgPreview 
            key={index} 
            src={img.url} 
            onDelete={() => handleDeleteImage(index)} 
>>>>>>> master
          />
        ))}
      </div>
    </div>
  );
};

ImgUpload.propTypes = {
  cameraIcon: PropTypes.string.isRequired,
  imgs: PropTypes.arrayOf(PropTypes.shape({
<<<<<<< HEAD
    src: PropTypes.string.isRequired,
  })).isRequired,
  handleImageUpload: PropTypes.func.isRequired,
  handleDeleteImage: PropTypes.func.isRequired,
  handleSetThumbnail: PropTypes.func.isRequired,
  isDisabled: PropTypes.bool, // isDisabled prop 추가
};

ImgUpload.defaultProps = {
  isDisabled: false, // 기본값은 false로 설정
=======
    url: PropTypes.string.isRequired,
  })).isRequired,
  handleImageUpload: PropTypes.func.isRequired,
  handleDeleteImage: PropTypes.func.isRequired,
>>>>>>> master
};

export default ImgUpload;
