export const TITLE_LENGTH = 60;
export const BODY_LENGTH = 100;

export const sliceContentLengthEndWithDots = (
  content: string,
  desiredLength: number
) => {
  if (content.length <= 67) return content;

  return content.slice(0, desiredLength - 3) + '...';
};

export const getFirstThreeChunks = (string: string) => {
  const chunks = string.split(' ');
  const firstThreeChunks = chunks.slice(0, 3).join(' ');
  return firstThreeChunks;
};
