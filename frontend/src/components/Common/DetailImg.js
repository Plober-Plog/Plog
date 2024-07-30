import React from 'react';
import PropTypes from 'prop-types';

const Image = ({ src, alt }) => {
    return (
      <div>
        <img src={src} alt={alt} />
      </div>
    )
};

export default Image;