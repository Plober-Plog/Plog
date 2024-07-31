import React from 'react';
import PropTypes from 'prop-types';


const Content = ({ content, setContent }) => {
  const handleChange = (event) => {
    setContent(event.target.value);
  };

  return (
    <div>
      <textarea 
        value={content} 
        onChange={handleChange} 
        placeholder="글을 입력하세요."
      />
    </div>
  );
};

Content.propTypes = {
  content: PropTypes.string.isRequired,
  setContent: PropTypes.func.isRequired,
};

export default Content;