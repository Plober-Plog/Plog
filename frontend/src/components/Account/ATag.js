const ATag = ({content, onClick}) => {
  return (
    <a onClick={onClick}>
      {content}
    </a>
  )
}

export default ATag;