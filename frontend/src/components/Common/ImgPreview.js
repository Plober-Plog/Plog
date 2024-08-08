import React from 'react';
import PropTypes from 'prop-types';
import Btn from './Btn';

const ImgPreview = ({ src, onDelete, isDisabled }) => {
  return (
    <div style={{ display: 'inline-block', margin: '10px' }}>
      <img src={src} alt="preview" style={{ width: '100px', height: '100px', objectFit: 'cover' }} />
      <div>
        <Btn 
          content="X" 
          type="button" 
          onClick={onDelete} 
          style={{ 
            background: isDisabled ? 'gray' : null,
            cursor: isDisabled ? 'not-allowed' : 'pointer'
          }}
          disabled={isDisabled}
        />
      </div>
    </div>
  );
};

ImgPreview.propTypes = {
  src: PropTypes.string.isRequired,
  onDelete: PropTypes.func.isRequired,
  isDisabled: PropTypes.bool,
};

ImgPreview.defaultProps = {
  isDisabled: false,
};

export default ImgPreview;
