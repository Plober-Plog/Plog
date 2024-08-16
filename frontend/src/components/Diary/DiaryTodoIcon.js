import React from 'react';
import PropTypes from 'prop-types';

<<<<<<< HEAD
const DiaryTodoIcon = ({ src, active, onClick }) => (
    <img
        src={src}
        alt="todo icon"
        style={{ width: '50px', height: '50px', opacity: active ? 1 : 0.5 , cursor: 'pointer'}}
=======
const DiaryTodoIcon = ({ src, fillSrc, active, onClick }) => (
    <img
        src={active ? fillSrc : src}
        alt="todo icon"
        style={{ width: '50px', height: '50px', cursor: 'pointer' }}
>>>>>>> master
        onClick={onClick}
    />
);

DiaryTodoIcon.propTypes = {
    src: PropTypes.string.isRequired,
<<<<<<< HEAD
=======
    fillSrc: PropTypes.string.isRequired,
>>>>>>> master
    active: PropTypes.bool.isRequired,
    onClick: PropTypes.func.isRequired,
};

<<<<<<< HEAD
export default DiaryTodoIcon;
=======
export default DiaryTodoIcon;
>>>>>>> master
