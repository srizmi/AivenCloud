-- sql statement to check existence of the given email in the table
select request_id
from shariz_challenge.signup_request
where email = ?;