import React from 'react';
import PropTypes from 'prop-types';
import Btn from './Btn';

const ImgPreview = ({ src, onDelete }) => {
  return (
    <div style={{ display: 'inline-block', margin: '10px' }}>
      <img src={src} alt="preview" style={{ width: '100px', height: '100px', objectFit: 'cover' }} />
      {/* 이미지가 너무 크게 보이지 않게 스타일 잠깐 적용함 */}
      <Btn text="X" type="button" onClick={onDelete} /> {/* onClick 이���트로 console.log() ��음 */}
    </div>
  );
};

ImgPreview.propTypes = {
  src: PropTypes.string.isRequired,
  onDelete: PropTypes.func.isRequired,
};

export default ImgPreview;