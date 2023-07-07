// 현재로부터 + a분의 Date 을 생성하는 함수
export const nowDateAfterSomeMinutes = (min: number) => {
  const nowDate = new Date();

  return new Date(nowDate.getTime() + min * 60000);
};

// Date 포맷을 input type="date"의 value 속성에 알맞은 포맷 'YYYY-MM-DD'으로 변경해주는 함수
export const dateToStringDate = (dateFormatDate: Date) => {
  // Date to 'YYYY-MM-DD'

  // add month to a zero
  const month = dateFormatDate.getMonth() + 1;
  const monthString = month < 10 ? `0${month}` : `${month}`;

  // add date to a zero
  const date = dateFormatDate.getDate();
  const dateString = date < 10 ? `0${date}` : `${date}`;

  const stringFormatDate = `${dateFormatDate.getFullYear()}-${monthString}-${dateString}`;
  return stringFormatDate;
};

// Date 포맷을 input type="time"의 value 속성에 알맞은 포맷 '00:00' 으로 변경해주는 함수
export const dateToStringTime = (dateFormatTime: Date) => {
  // Date time to '00:00'

  // add hour to a zero
  const hour = dateFormatTime.getHours();
  const hourString = hour < 10 ? `0${hour}` : `${hour}`;

  // add min to a zero
  const min = dateFormatTime.getMinutes();
  const minString = min < 10 ? `0${min}` : `${min}`;

  const stringFormatTime = `${hourString}:${minString}`;
  return stringFormatTime;
};
