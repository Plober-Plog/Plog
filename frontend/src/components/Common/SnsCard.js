import React from 'react';
import PropTypes from 'prop-types';

const Card = ({ image, text }) => {
    return (
      <div>
        <img src={image} />
        <p>{text}</p>
      </div>
    );
  };

export default SnsCard;