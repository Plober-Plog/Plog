import React from 'react';
import PropTypes from 'prop-types';
import Btn from './Btn';
import Img from './Img';

const ImgPreview = ({ src, isThumbnail, onSetThumbnail, onDelete }) => {
  return (
    <div style={{ display: 'inline-block', margin: '10px' }}>
      <img src={src} alt="preview" style={{ width: '100px', height: '100px', objectFit: 'cover' }} />
      <div>
        <Btn content="X" type="button" onClick={onDelete} />
      </div>
    </div>
  );
};

ImgPreview.propTypes = {
  src: PropTypes.string.isRequired,
  isThumbnail: PropTypes.bool.isRequired,
  onSetThumbnail: PropTypes.func.isRequired,
  onDelete: PropTypes.func.isRequired,
};

export default ImgPreview;
