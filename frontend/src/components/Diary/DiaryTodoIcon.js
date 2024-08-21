import React from 'react';
import PropTypes from 'prop-types';

const DiaryTodoIcon = ({ src, fillSrc, active, onClick }) => (
    <img
        src={active ? fillSrc : src}
        alt="todo icon"
        style={{ width: '50px', height: '50px', cursor: 'pointer' }}
        onClick={onClick}
    />
);

DiaryTodoIcon.propTypes = {
    src: PropTypes.string.isRequired,
    fillSrc: PropTypes.string.isRequired,
    active: PropTypes.bool.isRequired,
    onClick: PropTypes.func.isRequired,
};

export default DiaryTodoIcon;
