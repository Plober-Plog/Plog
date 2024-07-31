import React from 'react';

const RadioField = ({ selectedValue, onChange, options }) => {
  return (
    <div className="radio-field">
      {options.map(option => (
        <label key={option.value}>
          <input 
            type="radio" 
            value={option.value} 
            checked={selectedValue === option.value} 
            onChange={() => onChange(option.value)} 
          />
          {option.label}
        </label>
      ))}
    </div>
  );
}

export default RadioField;
