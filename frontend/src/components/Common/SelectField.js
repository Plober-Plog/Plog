import React from 'react';

// TODO [아영] 회원가입 필드 수정
const SelectField = ({ value, onChange, options, isRequired, className }) => {
  return (
    <div>
      <select
        value={value}
        onChange={onChange}
        required={isRequired}
        className={className}
      >
        {options.map(option => (
          <option key={option.value} value={option.value}>{option.label}</option>
        ))}
      </select>
    </div>
  );
}

export default SelectField;
