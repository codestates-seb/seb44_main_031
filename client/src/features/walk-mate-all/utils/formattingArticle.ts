export const TITLE_LENGTH = 67;
export const BODY_LENGTH = 120;

export const sliceContentLengthEndWithDots = (
  content: string,
  desiredLength: number
) => {
  if (content.length <= 67) return content;

  return content.slice(0, desiredLength - 3) + '...';
};

export const getFirstThreeChunks = (string: string) => {
  // return string.split(' ').slice(0, 3).join(' ');
  const chunks = string.split(' ');
  // console.log(chunks);
  const firstThreeChunks = chunks.slice(0, 3).join(' ');
  return firstThreeChunks;
};
