const ATag = ({content, onClick, className}) => {
  return (
    <a onClick={onClick} className={className}>
      {content}
    </a>
  )
}

export default ATag;