ALTER SEQUENCE travel_id_seq RESTART WITH 1;

INSERT
INTO
  travel
  (travel_id, destination, departure_date, return_date, budget, user_id)
VALUES
  (nextval('travel_id_seq'), 'Rio de Janeiro', '2023-02-17', '2023-02-21', 5000, 1);