import React from 'react';
import './Login.css'

const InputField = ({ type, placeholder, value, onChange, isrequired }) => {
  return (
    <div>
      <input 
        type={type} 
        placeholder={placeholder} 
        value={value} 
        onChange={onChange} 
        required={isrequired}
      />
    </div>
  );
}

export default InputField;
