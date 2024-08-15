import React from 'react';
import './RadioField.css'; // 스타일을 적용하기 위해 CSS 파일을 임포트합니다

const RadioField = ({ selectedValue, onChange, options }) => {
  return (
    <div className="radio-group">
      {options.map(option => (
        <label key={option.value} className="radio-label">
          <input 
            type="radio" 
            value={option.value} 
            checked={selectedValue === option.value} 
            onChange={() => onChange(option.value)} 
            className="radio-input"
          />
          <span className={`radio-custom ${selectedValue === option.value ? 'radio-selected' : ''}`}></span>
          {option.label}
        </label>
      ))}
    </div>
  );
}

export default RadioField;
