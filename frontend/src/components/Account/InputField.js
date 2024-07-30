import React from 'react';

function inputField({ type, placeholder, value, onChange, isrequired }) {
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

export default inputField;
